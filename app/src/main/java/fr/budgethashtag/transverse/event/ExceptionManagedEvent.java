package fr.budgethashtag.transverse.event;

import fr.budgethashtag.transverse.exception.BudgetHashtagException;

public class ExceptionManagedEvent {
    private BudgetHashtagException exceptionManaged;

    public ExceptionManagedEvent(BudgetHashtagException exceptionManaged) {
        this.exceptionManaged = exceptionManaged;
    }

    public BudgetHashtagException getExceptionManaged() {
        return exceptionManaged;
    }

    public void setExceptionManaged(BudgetHashtagException exceptionManaged) {
        this.exceptionManaged = exceptionManaged;
    }
}
