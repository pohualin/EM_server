package com.emmisolutions.emmimanager.model.user.admin;

import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;

/**
 * A user with administrative roles.
 */
@Entity
@Audited
@DiscriminatorValue("A")
public class UserAdmin extends User {

    @OneToMany(mappedBy = "userAdmin")
    private Set<UserAdminUserAdminRole> roles;

    /**
     * Calls super()
     */
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

    private transient volatile List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null){
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (UserAdminUserAdminRole userAdminUserAdminRole : getRoles()) {
                for (UserAdminPermission permission : userAdminUserAdminRole.getUserAdminRole().getPermissions()) {
                    authorityList.add(new SimpleGrantedAuthority(permission.getName().toString()));
                }
            }
            authorities = Collections.unmodifiableList(authorityList);
        }
        return authorities;
    }
}
