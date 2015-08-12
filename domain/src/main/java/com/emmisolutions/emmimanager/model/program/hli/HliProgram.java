package com.emmisolutions.emmimanager.model.program.hli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * An HLI program
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HliProgram implements Serializable {

    private String code;

    private int order;

    /**
     * This is the emmi code
     *
     * @return usually a number (which is the emmi code) but sometimes a string which isn't the emmi code
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "HliProgram{" +
                "code='" + code + '\'' +
                ", order=" + order +
                '}';
    }
}
