package com.emmisolutions.emmimanager.model.audit.logout;

import com.emmisolutions.emmimanager.model.user.User;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * A user logout record
 */
@Entity
@Table(name = "users_logout")
@XmlAccessorType(XmlAccessType.FIELD)
public class Logout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(name = "ip_address", columnDefinition = "nvarchar(512)")
    private String ipAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "users_logout_source_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_users_logout_users_logout_source"))
    private LogoutSource source;

    @ManyToOne
    @JoinColumn(name = "users_id",
            foreignKey = @ForeignKey(name = "fk_users_logout_users"), nullable = false)
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LogoutSource getSource() {
        return source;
    }

    public void setSource(LogoutSource source) {
        this.source = source;
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
}
