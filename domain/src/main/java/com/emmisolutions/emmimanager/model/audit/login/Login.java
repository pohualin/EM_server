package com.emmisolutions.emmimanager.model.audit.login;

import com.emmisolutions.emmimanager.model.user.User;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A login record
 */
@Entity
@Table(name = "users_login")
@XmlAccessorType(XmlAccessType.FIELD)
public class Login implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(name = "login", columnDefinition = "nvarchar(512)")
    private String login;

    @Column(name = "ip_address", columnDefinition = "nvarchar(512)")
    private String ipAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "users_login_status_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_users_login_users_login_status"))
    private LoginStatus status;

    @ManyToOne
    @JoinColumn(name = "users_id",
            foreignKey = @ForeignKey(name = "fk_users_login_users"))
    private User user;

    @NotNull
    @Column(name = "timestamp_utc", columnDefinition = "timestamp", nullable = false)
    private DateTime time;

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

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login that = (Login) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status=" + status +
                ", user=" + user +
                ", time=" + time +
                '}';
    }
}
