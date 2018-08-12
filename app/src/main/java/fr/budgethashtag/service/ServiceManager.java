package fr.budgethashtag.service;

import fr.budgethashtag.service.budget.BudgetService;
import fr.budgethashtag.service.portefeuille.PortefeuilleService;
import fr.budgethashtag.service.transaction.TransactionService;

import java.util.concurrent.ExecutorService;

public interface ServiceManager {
    void unbindAndDie();

    PortefeuilleService getPortefeuilleService();
    BudgetService getBudgetService();
    TransactionService getTransactionService();

    ExecutorService getCancelableThreadsExecutor();
    ExecutorService getKeepAliveThreadsExecutor();
}
