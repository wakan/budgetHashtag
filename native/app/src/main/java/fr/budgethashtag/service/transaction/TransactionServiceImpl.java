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
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.BudgetTransaction;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.budget.BudgetService;
import fr.budgethashtag.service.budget.BudgetServiceImpl;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import fr.budgethashtag.service.transaction.TransactionService;
import fr.budgethashtag.transverse.event.transaction.LoadTransacByIdPortefeuilleAndIdTransacResponseEvent;
import fr.budgethashtag.transverse.event.transaction.LoadTransacByIdPortefeuilleResponseEvent;
import fr.budgethashtag.transverse.event.transaction.SaveTransacResponseEvent;
import fr.budgethashtag.transverse.exception.BudgetHashtagException;
import fr.budgethashtag.transverse.exception.ExceptionManager;
import org.greenrobot.eventbus.EventBus;

import java.util.*;


public class TransactionServiceImpl extends MotherServiceImpl implements TransactionService {
    private static final String TAG = "TransactionService";
    private final PortefeuilleService portefeuilleService;
    private final BudgetService budgetService;
    private LoadTransacByIdPortefeuilleResponseEvent loadTransacByIdPortefeuilleResponseEvent;
    private LoadTransacByIdPortefeuilleAndIdTransacResponseEvent loadTransacByIdPortefeuilleAndIdTransacResponseEvent;
    private SaveTransacResponseEvent saveTransacResponseEvent;
    private Map<Integer, Map<Integer, ContentValues>> transacByIdPortefeuilleId = new HashMap<>();
    private Map<Integer, Boolean> isTransacCompletelyLoadedByIdPortefeuille = new HashMap<>();
    public TransactionServiceImpl(ServiceManager srvManager) {
        super(srvManager);
        //TODO : Injection
        portefeuilleService = new PortefeuilleServiceImpl(srvManager);
        budgetService = new BudgetServiceImpl(srvManager);
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

    @Override
    public void saveTransactionAsync(
            int id,
            String libelle,
            Date date, Double montant, List<Integer> budgetSupprime,
            List<String> budgetAjoutee,
            String locationProvider, Double longitude, Double latitude, Double altitude, Double accuracy
    ) {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new SaveTransactionRunnable(id, libelle, date, montant, budgetSupprime, budgetAjoutee,
                        locationProvider, longitude, latitude, altitude, accuracy));
    }
    private class SaveTransactionRunnable implements Runnable {
        private int id;
        private String libelle;
        private Date date;
        private Double montant;
        private List<Integer> budgetSupprime;
        private List<String> budgetAjoutee;
        private String locationProvider;
        private Double longitude;
        private Double latitude;
        private Double altitude;
        private Double accuracy;

        public SaveTransactionRunnable(int id,
                                       String libelle,
                                       Date date, Double montant, List<Integer> budgetSupprime,
                                       List<String> budgetAjoutee,
                                       String locationProvider, Double longitude, Double latitude, Double altitude,
                                       Double accuracy) {
            this.id = id;
            this.libelle = libelle;
            this.date = date;
            this.montant = montant;
            this.budgetSupprime = budgetSupprime;
            this.budgetAjoutee = budgetAjoutee;
            this.locationProvider = locationProvider;
            this.longitude = longitude;
            this.latitude = latitude;
            this.altitude = altitude;
            this.accuracy = accuracy;
        }
        @Override
        public void run(){
            saveTransacSync(id, libelle, date, montant , budgetSupprime, budgetAjoutee,
            locationProvider, longitude, latitude, altitude, accuracy);
        }
    }
    private void saveTransacSync(int id,
                                 String libelle, Date date, Double montant,
                                 List<Integer> budgetSupprime,  List<String> budgetAjoutee,
                                 String locationProvider, Double longitude, Double latitude,
                                 Double altitude, Double accuracy) {
        Log.d(TAG, "saveTransacSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        int idTransaction = insertTransaction(cr, id, idPortefeuille, libelle, montant, date,
                locationProvider, accuracy, altitude, latitude, longitude);
        budgetService.saveBudgetSync("", null, null);
        List<Integer> idsInsert = budgetService.findIdBudgetOrCreateIfNotExist(cr, idPortefeuille, budgetAjoutee);
        insertBudgetTransaction(cr, idPortefeuille, idTransaction, idsInsert);
        deleteBudgetTransaction(cr, idTransaction, idPortefeuille, budgetSupprime);

        //reload cache new transaction

        postSaveTransactionEvent(idPortefeuille, idTransaction);
    }

    private void postSaveTransactionEvent(int idPortefeuille, int id) {
        if(null == saveTransacResponseEvent){
            saveTransacResponseEvent = new SaveTransacResponseEvent();
        }
        Log.d(TAG, "postSaveTransactionEvent posted" );
        EventBus.getDefault().post(saveTransacResponseEvent);
    }

    private int insertTransaction(ContentResolver cr, int id, int idPortefeuille,
                                   String libelle, Double montant, Date date,
                                   String locationProvider, Double accuracy, Double altitude,
                                   Double latitude, Double longitude) {
        Uri uri = createTransaction(id, idPortefeuille, libelle, montant, date,
                locationProvider, accuracy, altitude, latitude, longitude);
        return getIdFromUri(uri);
    }

    private void insertBudgetTransaction(ContentResolver cr, int idTransaction, int idPortefeuille,
                                         List<Integer> budgetAjoutee) {
        for(Integer id : budgetAjoutee) {
            insertOneBudgetTransaction(cr, idPortefeuille, idTransaction, id);
        }
    }

    private void insertOneBudgetTransaction(ContentResolver cr, int idPortefeuille, int idTransaction,
                                            int idBudget) {
        ContentValues cv = new ContentValues();
        cv.put(BudgetTransaction.KEY_COL_ID_TRANSACTION, idTransaction);
        cv.put(BudgetTransaction.KEY_COL_ID_BUDGET, idBudget);
        Uri uriAdd = cr.insert(BudgetTransaction.contentUriCollection(idPortefeuille), cv);
        if(uriAdd == null)
            ExceptionManager.manage(new BudgetHashtagException(getClass(),
                    R.string.ex_msg_save_budget_in_transaction,
                    new OperationApplicationException()));
    }
    private void deleteBudgetTransaction(ContentResolver cr, int idPortefeuille, int idTransaction,
                                        List<Integer> ids) {
        for(Integer id : ids) {
            deleteOneBudgetTransaction(cr, idPortefeuille, idTransaction, id);
        }
    }
    private void deleteOneBudgetTransaction(ContentResolver cr, int idPortefeuille, int idTransaction,
                                            int idBudget) {
        String[] whereParams = {String.valueOf(idBudget), String.valueOf(idTransaction)};
        cr.delete(BudgetTransaction.contentUriCollection(idPortefeuille),
                BudgetTransaction.KEY_COL_ID_BUDGET + " = ? AND " + BudgetTransaction.KEY_COL_ID_TRANSACTION + " = ? ",
                whereParams);
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
    private Uri createTransaction(int id, int idPortefeuille, String libelle, Double montant,
                                        Date dateTransac, String locationProvider, Double locationAccuracy,
                                        Double locationAltitude, Double locationLatitude, Double locationLongitude) {
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
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
            ExceptionManager.manage(new BudgetHashtagException(getClass(),
                    R.string.ex_msg_save_transaction,
                    new OperationApplicationException()));
        return uri;
    }
    private int getIdFromUri(Uri uri) {
        return Integer.parseInt(Objects.requireNonNull(uri).getPathSegments().get(3));
    }

}
