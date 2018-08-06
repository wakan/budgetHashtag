package fr.budgethashtag.service.portefeuille;

import android.content.ContentValues;
import android.content.Context;
import fr.budgethashtag.service.MotherService;
import io.reactivex.Observable;

public interface PortefeuilleService extends MotherService {
    void getOrCreateDefaultPortefeuilleIfNotExistAsync();
    void getPortefeuilleByIdAsync();

    long getIdPortefeuilleFromSharedPref();
}
