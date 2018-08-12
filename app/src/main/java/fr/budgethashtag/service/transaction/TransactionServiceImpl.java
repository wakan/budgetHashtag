package fr.budgethashtag.service.transaction;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import fr.budgethashtag.service.transaction.TransactionService;
import fr.budgethashtag.transverse.event.transaction.LoadTransacByIdPortefeuilleAndIdTransacResponseEvent;
import fr.budgethashtag.transverse.event.transaction.LoadTransacByIdPortefeuilleResponseEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionServiceImpl extends MotherServiceImpl implements TransactionService {
    private static final String TAG = "TransactionService";
    private final PortefeuilleService portefeuilleService;
    private LoadTransacByIdPortefeuilleResponseEvent loadTransacByIdPortefeuilleResponseEvent;
    private LoadTransacByIdPortefeuilleAndIdTransacResponseEvent loadTransacByIdPortefeuilleAndIdTransacResponseEvent;
    private Map<Integer, Map<Integer, ContentValues>> transacByIdPortefeuilleId = new HashMap<>();
    private Map<Integer, Boolean> isTransacCompletelyLoadedByIdPortefeuille = new HashMap<>();
    public TransactionServiceImpl(ServiceManager srvManager) {
        super(srvManager);
        //TODO : Injection
        portefeuilleService = new PortefeuilleServiceImpl(srvManager);
    }
    @Override
    public void onDestroy() { }

    @Override
    public void loadTransacByIdPortefeuilleAsync() {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new LoadTransacByIdPortefeuilleRunnable());
    }
    private class LoadTransacByIdPortefeuilleRunnable implements Runnable {
        @Override
        public void run(){
            loadTransacByIdPortefeuilleSync();
        }
    }
    private void loadTransacByIdPortefeuilleSync() {
        Log.d(TAG, "loadTransacByIdPortefeuilleSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        if(!isTransacCompletelyLoadedByIdPortefeuille.containsKey(idPortefeuille)) {
            ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
            Cursor c = cr.query(Transaction.contentUriCollection(idPortefeuille),
                    null, null, null, null);
            if (c == null) {
                transacByIdPortefeuilleId.put(idPortefeuille, new HashMap<Integer, ContentValues>());
            }
            else {
                int nbObjectInCursor = Objects.requireNonNull(c).getCount();
                Map<Integer, ContentValues> transacById = new HashMap<>(nbObjectInCursor);
                try {
                    while (Objects.requireNonNull(c).moveToNext()) {
                        ContentValues cv = extractContentValueFromCursor(c);
                        transacById.put(cv.getAsInteger(Transaction.KEY_COL_ID), cv);
                    }
                } finally {
                    c.close();
                }
                transacByIdPortefeuilleId.put(idPortefeuille, transacById);
                isTransacCompletelyLoadedByIdPortefeuille.put(idPortefeuille, true);
            }
        }
        postLoadTransacByIdPortefeuilleEvent(idPortefeuille);
    }
    private void postLoadTransacByIdPortefeuilleEvent(int idPortefeuille) {
        Map<Integer, ContentValues> transacsById = transacByIdPortefeuilleId.get(idPortefeuille);
        if(null == loadTransacByIdPortefeuilleResponseEvent){
            loadTransacByIdPortefeuilleResponseEvent =
                    new LoadTransacByIdPortefeuilleResponseEvent(transacsById.values());
        }else{
            loadTransacByIdPortefeuilleResponseEvent.setLstContentValues(transacsById.values());
        }
        Log.d(TAG, "postLoadTransacByIdPortefeuilleEvent posted" );
        EventBus.getDefault().post(loadTransacByIdPortefeuilleResponseEvent);
    }

    @Override
    public void loadTransacByIdPortefeuilleAndIdTransacAsync(int id) {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new LoadTransacByIdPortefeuilleAndIdTransacRunnable(id));
    }
    private class LoadTransacByIdPortefeuilleAndIdTransacRunnable implements Runnable {
        private int id;
        public LoadTransacByIdPortefeuilleAndIdTransacRunnable(int id) {
            this.id = id;
        }
        @Override
        public void run(){
            loadTransacByIdPortefeuilleAndIdTransacSync(id);
        }
    }
    private void loadTransacByIdPortefeuilleAndIdTransacSync(int id) {
        Log.d(TAG, "loadTransacByIdPortefeuilleAndIdTransacSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        loadTransacByIdPortefeuilleAndIdTransacAndSetInCache(id, idPortefeuille);
        postLoadTransacByIdPortefeuilleAndIdTransacEvent(idPortefeuille, id);
    }
    private void loadTransacByIdPortefeuilleAndIdTransacAndSetInCache(int id, int idPortefeuille) {
        if (!transacByIdPortefeuilleId.containsKey(idPortefeuille)
                || null == transacByIdPortefeuilleId.get(idPortefeuille)
                || !transacByIdPortefeuilleId.get(idPortefeuille).containsKey(id)
        ) {
            ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
            try (Cursor c = cr.query(Transaction.contentUriItem(idPortefeuille, id),
                    null, null, null, null)) {
                Objects.requireNonNull(c).moveToNext();
                ContentValues contentValues = extractContentValueFromCursor(c);
                Map<Integer, ContentValues> transacsById = transacByIdPortefeuilleId.get(idPortefeuille);
                if(null == transacsById) {
                    transacsById = new HashMap<>();
                }
                transacsById.put(id, contentValues);
            }
        }
    }
    private void postLoadTransacByIdPortefeuilleAndIdTransacEvent(int idPortefeuille, int id) {
        ContentValues contentValues = transacByIdPortefeuilleId.get(idPortefeuille).get(id);
        if(null == loadTransacByIdPortefeuilleAndIdTransacResponseEvent){
            loadTransacByIdPortefeuilleAndIdTransacResponseEvent =
                    new LoadTransacByIdPortefeuilleAndIdTransacResponseEvent(contentValues);
        }else{
            loadTransacByIdPortefeuilleAndIdTransacResponseEvent.setContentValues(contentValues);
        }
        Log.d(TAG, "postLoadTransacByIdPortefeuilleAndIdTransacEvent posted" );
        EventBus.getDefault().post(loadTransacByIdPortefeuilleAndIdTransacResponseEvent);
    }



    @NonNull
    public static ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Transaction.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Transaction.KEY_COL_ID)));
        cv.put(Transaction.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_LIB)));
        cv.put(Transaction.KEY_COL_DT_VALEUR,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_DT_VALEUR)));
        cv.put(Transaction.KEY_COL_MONTANT,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_MONTANT)));
        cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(Transaction.KEY_COL_ID_PORTEFEUILLE)));
        cv.put(Transaction.KEY_COL_LOCATION_PROVIDER,
                c.getString(c.getColumnIndex(Transaction.KEY_COL_LOCATION_PROVIDER)));
        cv.put(Transaction.KEY_COL_LOCATION_ACCURACY,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_LOCATION_ACCURACY)));
        cv.put(Transaction.KEY_COL_LOCATION_ALTITUDE,
                c.getFloat(c.getColumnIndex(Transaction.KEY_COL_LOCATION_ALTITUDE)));
        cv.put(Transaction.KEY_COL_LOCATION_LATITUDE,
                c.getDouble(c.getColumnIndex(Transaction.KEY_COL_LOCATION_LATITUDE)));
        cv.put(Transaction.KEY_COL_LOCATION_LONGITUDE,
                c.getDouble(c.getColumnIndex(Transaction.KEY_COL_LOCATION_LONGITUDE)));
        return cv;
    }
    @NonNull
    private Uri createTransaction(Context context, long id, long idPortefeuille, String libelle, Double montant,
                                        Date dateTransac, String locationProvider, Double locationAccuracy,
                                        Double locationAltitude, Double locationLatitude, Double locationLongitude)
            throws OperationApplicationException {
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Transaction.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
        cv.put(Transaction.KEY_COL_LIB, libelle);
        cv.put(Transaction.KEY_COL_DT_VALEUR, dateTransac.getTime());
        cv.put(Transaction.KEY_COL_MONTANT, montant);
        Uri uri;
        if(id > 0) {
            int idUp = cr.update(Transaction.contentUriItem(idPortefeuille, id), cv, null, null);
            uri = Transaction.contentUriItem(idPortefeuille, idUp);
        } else {
            if (null != locationProvider) {
                cv.put(Transaction.KEY_COL_LOCATION_PROVIDER, locationProvider);
                cv.put(Transaction.KEY_COL_LOCATION_ACCURACY, locationAccuracy);
                cv.put(Transaction.KEY_COL_LOCATION_ALTITUDE, locationAltitude);
                cv.put(Transaction.KEY_COL_LOCATION_LATITUDE, locationLatitude);
                cv.put(Transaction.KEY_COL_LOCATION_LONGITUDE, locationLongitude);
            }
            uri = cr.insert(Transaction.contentUriCollection(idPortefeuille), cv);
        }
        if(uri == null)
            throw new OperationApplicationException(context.getString(R.string.ex_msg_save_budget));
        return uri;
    }


}
