package com.emmisolutions.emmimanager.model.user.client;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;

/**
 * UserClientPasswordHistory Domain Object
 * 
 */
@Audited
@Entity
@XmlRootElement(name = "user_client_password_history")
@Table(name = "user_client_password_history", uniqueConstraints = @UniqueConstraint(columnNames = {
        "user_client_id", "created_date" }, name = "uk_user_client_password_history"))
public class UserClientPasswordHistory extends AbstractAuditingEntity implements
        Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_client_id", nullable = false)
    private UserClient userClient;

    @Column(length = 40, columnDefinition = "varchar(40)")
    @Size(min = 0, max = 40)
    @XmlTransient
    private String password;

    @Column(length = 32, columnDefinition = "varchar(32)")
    @Size(min = 0, max = 32)
    @XmlTransient
    private String salt;

    /**
     * Default constructor
     */
    public UserClientPasswordHistory() {

    }

    /**
     * id constructor
     * 
     * @param id
     *            to use
     */
    public UserClientPasswordHistory(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserClientPasswordHistory that = (UserClientPasswordHistory) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
