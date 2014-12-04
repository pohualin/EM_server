package com.emmisolutions.emmimanager.model.user.admin;

import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * A user with administrative roles.
 */
@Entity
@Audited
@DiscriminatorValue("A")
public class UserAdmin extends User {

    @OneToMany(mappedBy = "userAdmin")
    private Set<UserAdminUserAdminRole> roles;

    public UserAdmin() {
        super();
    }

    /**
     * Constructor for security based things
     *
     * @param login    the login
     * @param password the password
     */
    public UserAdmin(String login, String password) {
        super(login, password);
    }

    /**
     * Constructor with ID only
     *
     * @param id      the id
     * @param version the version
     */
    public UserAdmin(Long id, Integer version) {
        super(id, version);
    }

    public Set<UserAdminUserAdminRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserAdminUserAdminRole> roles) {
        this.roles = roles;
    }
}
