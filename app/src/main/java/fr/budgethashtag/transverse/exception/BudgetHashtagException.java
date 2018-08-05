package fr.budgethashtag.transverse.exception;

import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;

public class BudgetHashtagException extends Exception {
    private final static int NO_ERROR_MESSAGE_ID = -1;
    private int errorMessageId = NO_ERROR_MESSAGE_ID;
    private String errorMessage = null;
    private boolean managed = false;
    private Class rootClass = null;
    private static final long serialVersionUID = 1L;

    public BudgetHashtagException(Class rootClass, Exception exc) {
        this(rootClass, NO_ERROR_MESSAGE_ID, exc);
    }
    public BudgetHashtagException(Class rootClass, int detailMessageId, Exception exc) {
        this(rootClass, loadMessage(detailMessageId), detailMessageId, exc);
    }
    private BudgetHashtagException(Class rootClass, String detailMessage, int detailMessageId, Exception exc) {
        super(detailMessage, exc);
        this.errorMessageId = detailMessageId;
        this.errorMessage = detailMessage;
        this.rootClass = rootClass;
    }
    public BudgetHashtagException(Class rootClass, Throwable throwable) {
        this(rootClass, NO_ERROR_MESSAGE_ID, throwable);
    }
    public BudgetHashtagException(Class rootClass, int detailMessageId, Throwable throwable) {
        this(rootClass, loadMessage(detailMessageId), detailMessageId, throwable);
    }
    private BudgetHashtagException(Class rootClass, String detailMessage, int detailMessageId, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessageId = detailMessageId;
        this.errorMessage = detailMessage;
        this.rootClass = rootClass;
    }
    public BudgetHashtagException(Class rootClass, int detailMessageId) {
        this(rootClass, loadMessage(detailMessageId), detailMessageId);
    }
    private BudgetHashtagException(Class rootClass, String detailMessage, int detailMessageId) {
        super(detailMessage);
        this.errorMessage = detailMessage;
        this.errorMessageId = detailMessageId;
        this.rootClass = rootClass;
    }

    private static String loadMessage(int errorMessageId) {
        if (errorMessageId != NO_ERROR_MESSAGE_ID) {
            return BudgetHashtagApplication.instance.getResources().getString(errorMessageId);
        } else {
            return BudgetHashtagApplication.instance.getResources().getString(R.string.no_error_message);
        }
    }


    public int getErrorMessageId() {
        return errorMessageId;
    }

    public void setErrorMessageId(int errorMessageId) {
        this.errorMessageId = errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    public Class getRootClass() {
        return rootClass;
    }

}
