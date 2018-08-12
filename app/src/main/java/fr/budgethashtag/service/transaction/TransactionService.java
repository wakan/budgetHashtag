package fr.budgethashtag.service.transaction;

import fr.budgethashtag.service.MotherService;

public interface TransactionService extends MotherService {
    void loadTransacByIdPortefeuilleAsync();
    void loadTransacByIdPortefeuilleAndIdTransacAsync(int id);

}
