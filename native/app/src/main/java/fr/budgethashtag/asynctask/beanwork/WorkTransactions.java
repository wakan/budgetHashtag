package fr.budgethashtag.asynctask.beanwork;

import java.util.ArrayList;
import java.util.List;

public class WorkTransactions {
    private final List<Integer> transactionsExistantesAjoutees = new ArrayList<>();
    private final List<Integer> transactionsExistantesSupprimees = new ArrayList<>();
    private final List<String> transactionsNouvelles = new ArrayList<>();

    public List<Integer> getTransactionsExistantesAjoutees() {
        return transactionsExistantesAjoutees;
    }
    public List<Integer> getTransactionsExistantesSupprimees() {
        return transactionsExistantesSupprimees;
    }
    public List<String> getTransactionsNouvelles() {
        return transactionsNouvelles;
    }
}
