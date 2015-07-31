package com.emmisolutions.emmimanager.model.audit.login;

import com.emmisolutions.emmimanager.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A login attempt by a user
 */
@Entity
@Table(name = "users_login_attempt")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginAttempt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(name = "login", columnDefinition = "nvarchar(512)")
    private String login;

    @Column(name = "ip_address", columnDefinition = "nvarchar(512)")
    private String ipAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "users_login_attempt_status_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_users_login_attempt_users_login_attempt_status"))
    private LoginAttemptStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_users_login_attempt_users"))
    private User user;

    @NotNull
    @Column(name = "timestamp_utc", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LoginAttemptStatus getStatus() {
        return status;
    }

    public void setStatus(LoginAttemptStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginAttempt that = (LoginAttempt) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoginAttempt{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status=" + status +
                ", user=" + user +
                ", time=" + time +
                '}';
    }
}
