package com.emmisolutions.emmimanager.model.audit.login;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A login status
 */
@Entity
@Table(name = "users_login_status")
@XmlAccessorType(XmlAccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LoginStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    private LoginStatusName name;

    public LoginStatus() {
    }

    /**
     * Create by name
     *
     * @param name of the status
     */
    public LoginStatus(LoginStatusName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoginStatusName getName() {
        return name;
    }

    public void setName(LoginStatusName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginStatus that = (LoginStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoginStatus{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
