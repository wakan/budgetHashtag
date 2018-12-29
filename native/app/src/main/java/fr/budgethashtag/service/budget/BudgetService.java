package fr.budgethashtag.service.budget;

import android.content.ContentResolver;
import fr.budgethashtag.service.MotherService;

import java.util.List;

public interface BudgetService extends MotherService {
    void loadBudgetByIdPortefeuilleAsync();
    void loadBudgetByIdPortefeuilleAndIdBudgetAsync(int id);
    void saveBudgetAsync(String libelle, double concurrency, String color);
    void saveBudgetSync(String libelle, Double concurrency, String color);
    List<Integer> findIdBudgetOrCreateIfNotExist(ContentResolver cr, int idPortefeuille, List<String> budgetsARetrouverOuAjouter);
}
