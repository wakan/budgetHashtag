package fr.budgethashtag.service.portefeuille;

import android.content.ContentValues;
import android.content.Context;
import fr.budgethashtag.service.MotherService;
import io.reactivex.Observable;

public interface PortefeuilleService extends MotherService {
    Observable<Long> getOrCreateDefaultPortefeuilleIfNotExistAsync(final Context context);
    Observable<Long> createDefaultPortefeuilleAsync(final Context context);
    Observable<ContentValues> getPortefeuilleByIdAsync(final Context context);

    long getIdPortefeuilleFromSharedPref(final Context context);
}
