package com.emmisolutions.emmimanager.model.salesforce;

/**
 * This represents a case type in salesforce
 */
public class CaseType {

    private String id;
    private String name;

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
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"id\":\"" + id + "\"" +
                ", \"name\":\"" + name + "\"" +
                '}';
    }
}
