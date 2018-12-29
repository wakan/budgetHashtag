package fr.budgethashtag.transverse.event;

public class ErrorEvent {
    private String errorMessage;
    private int errorMessageId;

    public ErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorEvent(int errorMessageId) {
        this.errorMessageId = errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorMessageId() {
        return errorMessageId;
    }

    public void setErrorMessageId(int errorMessageId) {
        this.errorMessageId = errorMessageId;
    }
}

