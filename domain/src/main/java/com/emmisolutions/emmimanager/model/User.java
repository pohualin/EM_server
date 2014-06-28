package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Audited
public class User extends AbstractAuditingEntity implements Serializable {

    private Long id;

    @Size(min = 0, max = 50)
    private String login;

    private Integer version;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return !(id != null ? !id.equals(user.id) : user.id != null) && !(version != null ? !version.equals(user.version) : user.version != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", version=" + version +
                '}';
    }
}
