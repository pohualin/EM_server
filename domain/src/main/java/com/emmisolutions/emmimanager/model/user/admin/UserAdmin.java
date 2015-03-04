package com.emmisolutions.emmimanager.model.user.admin;

import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * A user with administrative roles.
 */
@Entity
@Audited
@DiscriminatorValue("A")
@Table(name = "user_admin",
        indexes = {
                @Index(name = "ix_user_admin_email", columnList = "email", unique = true)
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"login"}, name = "uk_user_admin_login")
        }
)
public class UserAdmin extends User {

    @NotNull
    @Size(min = 0, max = 255)
    @Column(length = 255, nullable = false, columnDefinition = "nvarchar(255)")
    private String login;

    @Column(length = 40, columnDefinition = "varchar(40)")
    @Size(min = 0, max = 40)
    @XmlTransient
    private String password;

    @Column(length = 32, columnDefinition = "varchar(32)")
    @Size(min = 0, max = 32)
    @XmlTransient
    private String salt;

    @Email
    @Size(min = 0, max = 255)
    @Column(length = 255, columnDefinition = "nvarchar(255)")
    private String email;

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
        this.login = login;
        this.password = password;
    }

    /**
     * Constructor with ID only
     *
     * @param id      the id
     * @param version the version
     */
    public UserAdmin(Long id, Integer version) {
        setId(id);
        setVersion(version);
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
