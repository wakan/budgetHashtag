package fr.budgethashtag.service.transaction;

import fr.budgethashtag.service.MotherService;

import java.util.Date;
import java.util.List;

public interface TransactionService extends MotherService {
    void loadTransacByIdPortefeuilleAsync();
    void loadTransacByIdPortefeuilleAndIdTransacAsync(int id);
    void saveTransactionAsync(
            int id,
            String libelle,
            Date date, Double montant, List<Integer> budgetSupprime, List<String> budgetAjoute,
            String locationProvider, Double longitude, Double latitude, Double altitude, Double accuracy
            );

}
