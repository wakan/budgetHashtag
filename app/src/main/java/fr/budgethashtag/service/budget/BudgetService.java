package fr.budgethashtag.service.budget;

import fr.budgethashtag.service.MotherService;

public interface BudgetService extends MotherService {
    void loadBudgetByIdPortefeuilleAsync();
    void loadBudgetByIdPortefeuilleAndIdTransacAsync();
    void saveBudgetAsync();
}
