package fr.budgethashtag.service.transaction;

public interface TransactionService {
    void loadTransacByIdPortefeuilleAsync();
    void loadTransacByIdPortefeuilleAndIdTransacAsync(int id);

}
