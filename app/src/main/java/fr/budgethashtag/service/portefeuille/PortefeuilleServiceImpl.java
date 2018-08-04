package fr.budgethashtag.service.portefeuille;


import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.basecolumns.Portefeuille;
import fr.budgethashtag.helpers.PortefeuilleHelper;
import fr.budgethashtag.service.MotherService;
import fr.budgethashtag.service.MotherServiceImpl;
import fr.budgethashtag.service.ServiceManager;
import fr.budgethashtag.service.ServiceManagerImpl;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class PortefeuilleServiceImpl extends MotherServiceImpl
        implements PortefeuilleService {
    private static final String ID_PORTEFEULLE_SELECTED =  "IdPortefeuilleSelected";
    private static final int ID_NOT_EXIST = -1;

    public PortefeuilleServiceImpl(ServiceManager srvManager) {
        super(srvManager);
    }

    @Override
    public Observable<Long> getOrCreateDefaultPortefeuilleIfNotExistAsync(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> observer) throws Exception {
                try {
                    Long id = getOrCreateDefaultPortefeuilleIfNotExistSync(context);
                    observer.onNext(id);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.from(
                BudgetHashtagApplication.instance.getServiceManager()
                        .getKeepAliveThreadsExecutor()));
    }
    @Override
    public Observable<Long> createDefaultPortefeuilleAsync(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> observer) throws Exception {
                try {
                    Long id = createDefaultPortefeuilleSync(context);
                    observer.onNext(id);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.from(
                BudgetHashtagApplication.instance.getServiceManager()
                        .getKeepAliveThreadsExecutor()));
    }
    @Override
    public Observable<ContentValues> getPortefeuilleByIdAsync(final Context context) {
        return Observable.create(new ObservableOnSubscribe<ContentValues>() {
            @Override
            public void subscribe(ObservableEmitter<ContentValues> observer) throws Exception {
                try {
                    ContentValues value = getPortefeuilleByIdSync(context);
                    observer.onNext(value);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.from(
                BudgetHashtagApplication.instance.getServiceManager()
                        .getKeepAliveThreadsExecutor()));
    }

    private Long getOrCreateDefaultPortefeuilleIfNotExistSync(final Context context)
            throws OperationApplicationException {
        SharedPreferences appSharedPref =  context.getSharedPreferences(
                BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
        if(appSharedPref.contains(ID_PORTEFEULLE_SELECTED)) {
            return appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
        } else {
            long idPortefeuille = createDefaultPortefeuilleSync(context);
            SharedPreferences.Editor editor = appSharedPref.edit();
            editor.putLong(ID_PORTEFEULLE_SELECTED, idPortefeuille);
            editor.apply();
            return idPortefeuille;
        }
    }

    private Long createDefaultPortefeuilleSync(final Context context)
            throws OperationApplicationException {
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(Portefeuille.KEY_COL_LIB, context.getString(R.string.portefeuille_default_lib));
        Uri uriAdd = cr.insert(Portefeuille.contentUriCollection(), cv);
        if (uriAdd == null)
            throw new OperationApplicationException(context.getString(R.string.ex_msg_create_default_portefeuille));
        return Long.parseLong(Objects.requireNonNull(uriAdd).getPathSegments().get(1));
    }

    private ContentValues getPortefeuilleByIdSync(final Context context) {
        ContentResolver cr = context.getContentResolver();
        long idPortefeuille = getIdPortefeuilleFromSharedPref(context);
        ContentValues cv;
        try (Cursor c = cr.query(Portefeuille.contentUriItem(idPortefeuille),
                null, null, null, null)) {
            Objects.requireNonNull(c).moveToNext();
            cv = PortefeuilleHelper.extractContentValueFromCursor(c);
        }
        return cv;
    }

    @Override
    public long getIdPortefeuilleFromSharedPref(final Context context) {
        SharedPreferences appSharedPref =  context.getSharedPreferences(
                BudgetHashtagApplication.TAG, Context.MODE_PRIVATE);
        return appSharedPref.getLong(ID_PORTEFEULLE_SELECTED, ID_NOT_EXIST);
    }

    @Override
    public void onDestroy() { }

}
