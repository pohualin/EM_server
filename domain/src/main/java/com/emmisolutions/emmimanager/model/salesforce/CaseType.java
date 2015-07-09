package com.emmisolutions.emmimanager.model.salesforce;

import java.util.Objects;

/**
 * This represents a case type in salesforce
 */
public class CaseType {

    private String id;
    private String name;

    public CaseType() {
    }

    /**
     * CaseType constructed with its ID
     *
     * @param id the id
     */
    public CaseType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ", \"name\":\"" + name + "\"" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaseType caseType = (CaseType) o;
        return Objects.equals(id, caseType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
