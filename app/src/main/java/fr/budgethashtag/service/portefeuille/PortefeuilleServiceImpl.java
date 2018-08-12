package fr.budgethashtag.service.portefeuille;


import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.transverse.event.portefeuille.GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent;
import fr.budgethashtag.transverse.event.portefeuille.GetPortefeuilleByIdResponseEvent;
import fr.budgethashtag.transverse.exception.BudgetHashtagException;
import fr.budgethashtag.transverse.exception.ExceptionManager;
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
    @Override
    public void onDestroy() { }

    int idPortefeuilleCurrent;
    GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent getOrCreateDefaultPortefeuilleIfNotExistResponseEvent;
    Map<Integer, ContentValues> portefeuilleById = new HashMap<>();
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
            idPortefeuilleCurrent = appSharedPref.getInt(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        } else {
            int idPortefeuille = createDefaultPortefeuille();
            SharedPreferences.Editor editor = appSharedPref.edit();
            editor.putInt(ID_PORTEFEULLE_SELECTED, idPortefeuille);
            editor.apply();
            idPortefeuilleCurrent = idPortefeuille;
        }
        postGetOrCreateDefaultPortefeuilleIfNotExistEvent();
    }
    private Integer createDefaultPortefeuille() {
        ContentResolver cr = BudgetHashtagApplication.instance.getContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_LIB, BudgetHashtagApplication.instance.getContext()
                .getString(R.string.portefeuille_default_lib));
        Uri uriAdd = cr.insert(Portefeuille.contentUriCollection(), cv);
        if (uriAdd == null)
            ExceptionManager.manage(new BudgetHashtagException(getClass(),
                    R.string.ex_msg_create_default_portefeuille,
                    new OperationApplicationException()));
        return getIdFromUri(uriAdd);
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
            int idPortefeuille = getIdPortefeuilleFromSharedPref();
            idPortefeuilleCurrent = idPortefeuille;
        }
        ContentValues cv;
        if(!portefeuilleById.containsKey(idPortefeuilleCurrent)) {
            try (Cursor c = cr.query(Portefeuille.contentUriItem(idPortefeuilleCurrent),
                    null, null, null, null)) {
                Objects.requireNonNull(c).moveToNext();
                cv = extractContentValueFromCursor(c);
            }
            portefeuilleById.put(idPortefeuilleCurrent, cv);
        }
        postGetPortefeuilleByIdEvent();
    }
    private void postGetPortefeuilleByIdEvent() {
        if(getPortefeuilleByIdResponseEvent == null){
            getPortefeuilleByIdResponseEvent =
                    new GetPortefeuilleByIdResponseEvent(portefeuilleById.get(idPortefeuilleCurrent));
        }else{
            getPortefeuilleByIdResponseEvent.setContentValues(portefeuilleById.get(idPortefeuilleCurrent));
        }
        Log.d(TAG, "postGetPortefeuilleByIdEvent posted" );
        EventBus.getDefault().post(getPortefeuilleByIdResponseEvent);
    }

    @Override
    public int getIdPortefeuilleFromSharedPref() {
        if(isPortefeuilleCurrentNotInitialized()) {
            SharedPreferences appSharedPref = BudgetHashtagApplication.instance.getContext()
                    .getSharedPreferences(
                            BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
            idPortefeuilleCurrent = appSharedPref.getInt(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        }
        return idPortefeuilleCurrent;
    }

    private boolean isPortefeuilleCurrentNotInitialized() {
        return 0 == idPortefeuilleCurrent;
    }

    @NonNull
    private ContentValues extractContentValueFromCursor(Cursor c) {
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_ID,
                c.getInt(c.getColumnIndex(Portefeuille.KEY_COL_ID)));
        cv.put(Portefeuille.KEY_COL_LIB,
                c.getString(c.getColumnIndex(Portefeuille.KEY_COL_LIB)));
        return cv;
    }

    private int getIdFromUri(Uri uriAdd) {
        return Integer.parseInt(Objects.requireNonNull(uriAdd).getPathSegments().get(1));
    }

}
