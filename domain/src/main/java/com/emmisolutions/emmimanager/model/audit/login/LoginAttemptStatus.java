package com.emmisolutions.emmimanager.model.audit.login;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A login attempt status
 */
@Entity
@Table(name = "users_login_attempt_status")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginAttemptStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    private LoginAttemptStatusName name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoginAttemptStatusName getName() {
        return name;
    }

    public void setName(LoginAttemptStatusName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginAttemptStatus that = (LoginAttemptStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoginAttemptStatus{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
