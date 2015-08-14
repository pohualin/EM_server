package com.emmisolutions.emmimanager.model.program.hli.remote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * An HLI program holder for JSON responses
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HliProgramJson implements Serializable {

    private String code;

    private int weight;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
