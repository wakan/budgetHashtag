package fr.budgethashtag.service.budget;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import fr.budgethashtag.transverse.event.budget.LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent;
import fr.budgethashtag.transverse.event.budget.LoadBudgetByIdPortefeuilleResponseEvent;
import fr.budgethashtag.transverse.event.budget.SaveBudgetResponseEvent;
import fr.budgethashtag.transverse.exception.BudgetHashtagException;
import fr.budgethashtag.transverse.exception.ExceptionManager;
import org.greenrobot.eventbus.EventBus;

import java.util.*;

public class BudgetServiceImpl extends MotherServiceImpl implements BudgetService {
    private static final String TAG = "BudgetService";
    private final PortefeuilleService portefeuilleService;
    private LoadBudgetByIdPortefeuilleResponseEvent loadBudgetByIdPortefeuilleResponseEvent;
    private LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent;
    private SaveBudgetResponseEvent saveBudgetResponseEvent;
    private Map<Integer, Map<Integer, ContentValues>> budgetByIdPortefeuilleId = new HashMap<>();
    private Map<Integer, Boolean> isBudgetCompletelyLoadedByIdPortefeuille = new HashMap<>();
    public BudgetServiceImpl(ServiceManager srvManager) {
        super(srvManager);
        //TODO : Injection
        portefeuilleService = new PortefeuilleServiceImpl(srvManager);
    }
    @Override
    public void onDestroy() { }

    @Override
    public void loadBudgetByIdPortefeuilleAsync() {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new LoadBudgetByIdPortefeuilleRunnable());
    }
    private class LoadBudgetByIdPortefeuilleRunnable implements Runnable {
        @Override
        public void run(){
            loadBudgetByIdPortefeuilleSync();
        }
    }
    private void loadBudgetByIdPortefeuilleSync() {
        Log.d(TAG, "loadBudgetByIdPortefeuilleSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        if(!isBudgetCompletelyLoadedByIdPortefeuille.containsKey(idPortefeuille)) {
            ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
            Cursor c = cr.query(Budget.contentUriCollection(idPortefeuille),
                    null, null, null, null);
            if (c == null) {
                budgetByIdPortefeuilleId.put(idPortefeuille, new HashMap<Integer, ContentValues>());
            }
            else {
                int nbObjectInCursor = Objects.requireNonNull(c).getCount();
                Map<Integer, ContentValues> budgetById = new HashMap<>(nbObjectInCursor);
                try {
                    while (Objects.requireNonNull(c).moveToNext()) {
                        ContentValues cv = extractContentValueFromCursor(c);
                        budgetById.put(cv.getAsInteger(Budget.KEY_COL_ID), cv);
                    }
                } finally {
                    c.close();
                }
                budgetByIdPortefeuilleId.put(idPortefeuille, budgetById);
                isBudgetCompletelyLoadedByIdPortefeuille.put(idPortefeuille, true);
            }
        }
        postLoadBudgetByIdPortefeuilleEvent(idPortefeuille);
    }
    private void postLoadBudgetByIdPortefeuilleEvent(int idPortefeuille) {
        Map<Integer, ContentValues> budgetsById = budgetByIdPortefeuilleId.get(idPortefeuille);
        if(null == loadBudgetByIdPortefeuilleResponseEvent){
            loadBudgetByIdPortefeuilleResponseEvent =
                    new LoadBudgetByIdPortefeuilleResponseEvent(budgetsById.values());
        }else{
            loadBudgetByIdPortefeuilleResponseEvent.setContentValues(budgetsById.values());
        }
        Log.d(TAG, "postLoadBudgetByIdPortefeuilleEvent posted" );
        EventBus.getDefault().post(loadBudgetByIdPortefeuilleResponseEvent);
    }

    @Override
    public void loadBudgetByIdPortefeuilleAndIdBudgetAsync(int id) {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new LoadBudgetByIdPortefeuilleAndIdBudgetRunnable(id));
    }
    private class LoadBudgetByIdPortefeuilleAndIdBudgetRunnable implements Runnable {
        private int id;
        public LoadBudgetByIdPortefeuilleAndIdBudgetRunnable(int id) {
            this.id = id;
        }
        @Override
        public void run(){
            loadBudgetByIdPortefeuilleAndIdBudgetSync(id);
        }
    }
    private void loadBudgetByIdPortefeuilleAndIdBudgetSync(int id) {
        Log.d(TAG, "loadBudgetByIdPortefeuilleAndIdBudgetSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        loadBudgetByIdPortefeuilleAndIdBudgetAndSetInCache(id, idPortefeuille);
        postLoadBudgetByIdPortefeuilleAndIdBudgetEvent(idPortefeuille, id);
    }
    private void loadBudgetByIdPortefeuilleAndIdBudgetAndSetInCache(int id, int idPortefeuille) {
        if (!budgetByIdPortefeuilleId.containsKey(idPortefeuille)
                || null == budgetByIdPortefeuilleId.get(idPortefeuille)
                || !budgetByIdPortefeuilleId.get(idPortefeuille).containsKey(id)
        ) {
            ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
            try (Cursor c = cr.query(Budget.contentUriItem(idPortefeuille, id),
                    null, null, null, null)) {
                Objects.requireNonNull(c).moveToNext();
                ContentValues contentValues = extractContentValueFromCursor(c);
                Map<Integer, ContentValues> budgetsById = budgetByIdPortefeuilleId.get(idPortefeuille);
                if(null == budgetsById) {
                    budgetsById = new HashMap<>();
                }
                budgetsById.put(id, contentValues);
            }
        }
    }
    private void postLoadBudgetByIdPortefeuilleAndIdBudgetEvent(int idPortefeuille, int id) {
        ContentValues contentValues = budgetByIdPortefeuilleId.get(idPortefeuille).get(id);
        if(null == loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent){
            loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent =
                    new LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent(contentValues);
        }else{
            loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent.setContentValues(contentValues);
        }
        Log.d(TAG, "postLoadBudgetByIdPortefeuilleAndIdBudgetEvent posted" );
        EventBus.getDefault().post(loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent);
    }

    @Override
    public void saveBudgetAsync(String libelle, double concurrency, String color) {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new SaveBudgetRunnable(libelle, concurrency, color));
    }
    private class SaveBudgetRunnable implements Runnable {
        private String libelle;
        private  double concurrency;
        private String color;
        public SaveBudgetRunnable(String libelle, double concurrency, String color) {
            this.libelle = libelle;
            this.concurrency = concurrency;
            this.color = color;
        }
        @Override
        public void run(){
            saveBudgetSync(libelle, concurrency, color);
        }
    }
    @Override
    public void saveBudgetSync(String libelle, Double concurrency, String color) {
        Log.d(TAG, "saveBudgetSync() called");
        int idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        Uri uri = createBudget(idPortefeuille, libelle, concurrency, color);
        int id = getIdFromUri(uri);
        //Need for cache. If not, new value not in cache and not load next time loadByIdPortefeuille
        loadBudgetByIdPortefeuilleAndIdBudgetAndSetInCache(id, idPortefeuille);
        postSaveBudgetEvent(idPortefeuille, id);
    }
    private void postSaveBudgetEvent(int idPortefeuille, int id) {
        if(null == saveBudgetResponseEvent){
            saveBudgetResponseEvent = new SaveBudgetResponseEvent();
        }
        Log.d(TAG, "postSaveBudgetEvent posted" );
        EventBus.getDefault().post(saveBudgetResponseEvent);
    }

    private int getIdFromUri(Uri uri) {
        return Integer.parseInt(Objects.requireNonNull(uri).getPathSegments().get(3));
    }
    @NonNull
    private ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Budget.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Budget.KEY_COL_ID)));
        cv.put(Budget.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Budget.KEY_COL_LIB)));
        cv.put(Budget.KEY_COL_COLOR,
                c.getString(c.getColumnIndex(Budget.KEY_COL_COLOR)));
        cv.put(Budget.KEY_COL_PREVISIONNEL,
                c.getFloat(c.getColumnIndex(Budget.KEY_COL_PREVISIONNEL)));
        cv.put(Budget.KEY_COL_ID_PORTEFEUILLE,
                c.getInt(c.getColumnIndex(Budget.KEY_COL_ID_PORTEFEUILLE)));
        if(c.getColumnCount() > 5) {
            cv.put(Budget.KEY_COL_EXP_SUM_MNT,
                    c.getInt(c.getColumnIndex(Budget.KEY_COL_EXP_SUM_MNT)));
            cv.put(Budget.KEY_COL_EXP_COUNT_MNT,
                    c.getInt(c.getColumnIndex(Budget.KEY_COL_EXP_COUNT_MNT)));
        }
        return cv;
    }
    @NonNull
    private Uri createBudget(long idPortefeuille, String libelle, Double montantPrevi, String color) {
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Budget.KEY_COL_LIB, libelle);
        cv.put(Budget.KEY_COL_PREVISIONNEL, montantPrevi);
        cv.put(Budget.KEY_COL_ID_PORTEFEUILLE, idPortefeuille);
        cv.put(Budget.KEY_COL_COLOR, color);
        Uri uriAdd = cr.insert(Budget.contentUriCollection(idPortefeuille),cv);
        if(uriAdd == null)
            ExceptionManager.manage(new BudgetHashtagException(getClass(),
                    R.string.ex_msg_save_budget,
                    new OperationApplicationException()));
        return uriAdd;
    }

}
