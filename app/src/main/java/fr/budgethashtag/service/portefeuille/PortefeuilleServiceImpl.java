package fr.budgethashtag.service.portefeuille;


import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.transverse.event.portefeuille.GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent;
import fr.budgethashtag.transverse.event.portefeuille.GetPortefeuilleByIdResponseEvent;
import fr.budgethashtag.transverse.exception.BudgetHashtagException;
import fr.budgethashtag.transverse.exception.ExceptionManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PortefeuilleServiceImpl extends MotherServiceImpl
        implements PortefeuilleService {
    private static final String TAG = "PortefeuilleService";
    private static final String ID_PORTEFEULLE_SELECTED =  "IdPortefeuilleSelected";
    private static final int ID_NOT_EXIST = -1;

    public PortefeuilleServiceImpl(ServiceManager srvManager) {
        super(srvManager);
    }

    long idPortefeuilleCurrent;
    GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent getOrCreateDefaultPortefeuilleIfNotExistResponseEvent;
    Map<Long, ContentValues> portefeuilleById = new HashMap<>();
    GetPortefeuilleByIdResponseEvent getPortefeuilleByIdResponseEvent;

    @Override
    public void getOrCreateDefaultPortefeuilleIfNotExistAsync() {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new GetOrCreateDefaultPortefeuilleIfNotExistRunnable());
    }
    private class GetOrCreateDefaultPortefeuilleIfNotExistRunnable implements Runnable {
        @Override
        public void run(){
            getOrCreateDefaultPortefeuilleIfNotExistSync();
        }
    }
    private void getOrCreateDefaultPortefeuilleIfNotExistSync() {
        Log.d(TAG, "getOrCreateDefaultPortefeuilleIfNotExistSync() called");
        SharedPreferences appSharedPref =  BudgetHashtagApplication.instance.getContext().getSharedPreferences(
                BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
        if(appSharedPref.contains(ID_PORTEFEULLE_SELECTED)) {
            idPortefeuilleCurrent = appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        } else {
            long idPortefeuille = createDefaultPortefeuille();
            SharedPreferences.Editor editor = appSharedPref.edit();
            editor.putLong(ID_PORTEFEULLE_SELECTED, idPortefeuille);
            editor.apply();
            idPortefeuilleCurrent = idPortefeuille;
        }
        postGetOrCreateDefaultPortefeuilleIfNotExistEvent();
    }
    private Long createDefaultPortefeuille() {
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_LIB, BudgetHashtagApplication.instance.getContext()
                .getString(R.string.portefeuille_default_lib));
        Uri uriAdd = cr.insert(Portefeuille.contentUriCollection(), cv);
        if (uriAdd == null)
            ExceptionManager.manage(new BudgetHashtagException(getClass(),
                    R.string.ex_msg_create_default_portefeuille,
                    new OperationApplicationException()));
        return Long.parseLong(Objects.requireNonNull(uriAdd).getPathSegments().get(1));
    }
    private void postGetOrCreateDefaultPortefeuilleIfNotExistEvent() {
        if(getOrCreateDefaultPortefeuilleIfNotExistResponseEvent ==null){
            getOrCreateDefaultPortefeuilleIfNotExistResponseEvent =
                    new GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent(idPortefeuilleCurrent);
        }else{
            getOrCreateDefaultPortefeuilleIfNotExistResponseEvent.setIdPortefeuille(idPortefeuilleCurrent);
        }
        Log.d(TAG, "GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent posted" );
        EventBus.getDefault().post(getOrCreateDefaultPortefeuilleIfNotExistResponseEvent);
    }

    @Override
    public void getPortefeuilleByIdAsync() {
        BudgetHashtagApplication.instance.getServiceManager().getCancelableThreadsExecutor()
                .submit(new GetPortefeuilleByIdRunnable());
    }
    private class GetPortefeuilleByIdRunnable implements Runnable {
        @Override
        public void run(){
            getPortefeuilleByIdSync();
        }
    }
    private void getPortefeuilleByIdSync() {
        Log.d(TAG, "getPortefeuilleByIdSync() called");
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        if(isPortefeuilleCurrentNotInitialized()) {
            long idPortefeuille = getIdPortefeuilleFromSharedPref();
            idPortefeuilleCurrent = idPortefeuille;
        }
        ContentValues cv;
        try (Cursor c = cr.query(Portefeuille.contentUriItem(idPortefeuilleCurrent),
                null, null, null, null)) {
            Objects.requireNonNull(c).moveToNext();
            cv = PortefeuilleHelper.extractContentValueFromCursor(c);
        }
        portefeuilleById.put(idPortefeuilleCurrent, cv);
        postGetPortefeuilleByIdEvent();
    }
    private void postGetPortefeuilleByIdEvent() {
        if(getPortefeuilleByIdResponseEvent ==null){
            getPortefeuilleByIdResponseEvent =
                    new GetPortefeuilleByIdResponseEvent(portefeuilleById.get(idPortefeuilleCurrent));
        }else{
            getPortefeuilleByIdResponseEvent.setContentValues(portefeuilleById.get(idPortefeuilleCurrent));
        }
        Log.d(TAG, "postGetPortefeuilleByIdEvent posted" );
        EventBus.getDefault().post(getPortefeuilleByIdResponseEvent);
    }

    @Override
    public long getIdPortefeuilleFromSharedPref() {
        if(isPortefeuilleCurrentNotInitialized()) {
            SharedPreferences appSharedPref = BudgetHashtagApplication.instance.getContext()
                    .getSharedPreferences(
                            BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
            idPortefeuilleCurrent = appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        }
        return idPortefeuilleCurrent;
    }

    private boolean isPortefeuilleCurrentNotInitialized() {
        return idPortefeuilleCurrent == 0L;
    }

    @Override
    public void onDestroy() { }

}
