package fr.budgethashtag.service.budget;

import fr.budgethashtag.service.MotherService;

public interface BudgetService extends MotherService {
    void loadBudgetByIdPortefeuilleAsync();
    void loadBudgetByIdPortefeuilleAndIdBudgetAsync(int id);
    void saveBudgetAsync(String libelle, double concurrency, String color);
    void saveBudgetSync(String libelle, Double concurrency, String color);
    Integer findBudgetExactByLibelle(String libelle);
}
