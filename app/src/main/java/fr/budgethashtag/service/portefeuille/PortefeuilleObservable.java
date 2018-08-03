package fr.budgethashtag.service.portefeuille;

import android.content.ContentValues;
import android.content.Context;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class PortefeuilleObservable {

    private static final PortefeuilleService portefeuilleService = new PortefeuilleServiceImpl();

    public static Observable<Long> getOrCreateDefaultPortefeuilleIfNotExist(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> observer) throws Exception {
                try {
                    Long id = portefeuilleService.getOrCreateDefaultPortefeuilleIfNotExist(context);
                    observer.onNext(id);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<Long> createDefaultPortefeuille(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> observer) throws Exception {
                try {
                    Long id = portefeuilleService.createDefaultPortefeuille(context);
                    observer.onNext(id);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<ContentValues> getPortefeuilleById(final Context context) {
        return Observable.create(new ObservableOnSubscribe<ContentValues>() {
            @Override
            public void subscribe(ObservableEmitter<ContentValues> observer) throws Exception {
                try {
                    ContentValues value = portefeuilleService.getPortefeuilleById(context);
                    observer.onNext(value);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
