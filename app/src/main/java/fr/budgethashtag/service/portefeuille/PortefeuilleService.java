package fr.budgethashtag.service.portefeuille;

import fr.budgethashtag.service.MotherService;

public interface PortefeuilleService extends MotherService {
    void getOrCreateDefaultPortefeuilleIfNotExistAsync();
    void getPortefeuilleByIdAsync();

    long getIdPortefeuilleFromSharedPref();
}
