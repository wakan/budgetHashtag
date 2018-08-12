package fr.budgethashtag.service.budget;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.basecolumns.Transaction;
import fr.budgethashtag.helpers.TransactionHelper;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import fr.budgethashtag.transverse.event.budget.LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent;
import fr.budgethashtag.transverse.event.budget.LoadBudgetByIdPortefeuilleResponseEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.*;

public class BudgetServiceImpl extends MotherServiceImpl implements BudgetService {
    private static final String TAG = "BudgetService";
    private final PortefeuilleService portefeuilleService;
    private LoadBudgetByIdPortefeuilleResponseEvent loadBudgetByIdPortefeuilleResponseEvent;
    private LoadBudgetByIdPortefeuilleAndIdBudgetResponseEvent loadBudgetByIdPortefeuilleAndIdBudgetResponseEvent;
    private Map<Integer, Map<Integer, ContentValues>> budgetByIdPortefeuilleId = new HashMap<>();
    private Map<Integer, Boolean> isbudgetCompletelyLoadedByIdPortefeuille = new HashMap<>();
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
        if(!isbudgetCompletelyLoadedByIdPortefeuille.containsKey(idPortefeuille)) {
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
                isbudgetCompletelyLoadedByIdPortefeuille.put(idPortefeuille, true);
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
        if (!budgetByIdPortefeuilleId.containsKey(idPortefeuille)
                || null == budgetByIdPortefeuilleId.get(idPortefeuille)
                || !budgetByIdPortefeuilleId.get(idPortefeuille).containsKey(id)
        ) {
            ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
            try (Cursor c = cr.query(Budget.contentUriItem(idPortefeuille, id),
                    null, null, null, null)) {
                Objects.requireNonNull(c).moveToNext();
                ContentValues contentValues = extractContentValueFromCursor(c);

            }
        }
        postLoadBudgetByIdPortefeuilleAndIdBudgetEvent(idPortefeuille, id);
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
    public void saveBudgetAsync() {

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



}
