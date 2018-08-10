package fr.budgethashtag.service.budget;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.basecolumns.Budget;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.portefeuille.PortefeuilleServiceImpl;
import fr.budgethashtag.transverse.event.budget.LoadBudgetByIdPortefeuilleResponseEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BudgetServiceImpl extends MotherServiceImpl implements BudgetService {
    private static final String TAG = "BudgetService";
    private final PortefeuilleService portefeuilleService;
    private LoadBudgetByIdPortefeuilleResponseEvent loadBudgetByIdPortefeuilleResponseEvent;
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
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        long idPortefeuille = portefeuilleService.getIdPortefeuilleFromSharedPref();
        Cursor c = cr.query(Budget.contentUriCollection(idPortefeuille),
                null, null, null, null);
        List<ContentValues> ret = null;
        if(c == null)
            ret = new ArrayList<>();
        else {
            ret = new ArrayList<>(Objects.requireNonNull(c).getCount());
            try {
                while (Objects.requireNonNull(c).moveToNext()) {
                    ContentValues cv = extractContentValueFromCursor(c);
                    ret.add(cv);
                }
            } finally {
                c.close();
            }
        }
        postLoadBudgetByIdPortefeuilleEvent(ret);
    }
    private void postLoadBudgetByIdPortefeuilleEvent(List<ContentValues> ret) {
        if(loadBudgetByIdPortefeuilleResponseEvent ==null){
            loadBudgetByIdPortefeuilleResponseEvent =
                    new LoadBudgetByIdPortefeuilleResponseEvent(ret);
        }else{
            loadBudgetByIdPortefeuilleResponseEvent.setContentValues(ret);
        }
        Log.d(TAG, "postLoadBudgetByIdPortefeuilleEvent posted" );
        EventBus.getDefault().post(loadBudgetByIdPortefeuilleResponseEvent);
    }

    @Override
    public void loadBudgetByIdPortefeuilleAndIdTransacAsync() {

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
