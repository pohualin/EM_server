package com.emmisolutions.emmimanager.model.salesforce;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a save case call.
 */
public class CaseSaveResult {

    private boolean success;
    private String id;
    private List<String> errorMessages;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public CaseSaveResult addErrorMessage(String errorMessage) {
        if (errorMessages == null) {
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(errorMessage);
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
