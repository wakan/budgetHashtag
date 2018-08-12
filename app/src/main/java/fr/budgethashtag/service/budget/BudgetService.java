package fr.budgethashtag.service.budget;

import fr.budgethashtag.service.MotherService;

public interface BudgetService extends MotherService {
    void loadBudgetByIdPortefeuilleAsync();
    void loadBudgetByIdPortefeuilleAndIdBudgetAsync(int id);
    void saveBudgetAsync();
}
