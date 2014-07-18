package com.emmisolutions.emmimanager.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is an application permission.
 */
@Entity
@Table(name = "permission")
@XmlRootElement(name = "permission")
@XmlAccessorType(XmlAccessType.FIELD)
public class Permission {

    @Id
    @NotNull
    @Size(min = 0, max = 100)
    @Enumerated(EnumType.STRING)
    private PermissionEnum name;

    public PermissionEnum getName() {
        return name;
    }

    public void setName(PermissionEnum name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name=" + name +
                '}';
    }
}
