package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a save case call.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CaseSaveResult {

    private boolean success;

    private String id;

    @XmlElement(name = "errorMessage")
    @XmlElementWrapper(name = "errorMessages")
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

    @Override
    public String toString() {
        return "CaseSaveResult{" +
                "success=" + success +
                ", id='" + id + '\'' +
                ", errorMessages=" + errorMessages +
                '}';
    }
}
