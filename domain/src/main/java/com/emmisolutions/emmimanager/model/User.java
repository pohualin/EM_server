package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 * A user represents a person who logs in to the system and uses it.
 */
@Audited
@Entity
@Table(name = "app_user",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"login"}))
@XmlRootElement(name = "user")
public class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(length = 50, nullable = false)
    @XmlTransient
    private String login;

    @ManyToMany
    @JoinTable(
            name = "app_user_role",
            joinColumns = {@JoinColumn(name = "app_user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")})
    @XmlTransient
    private Set<Role> roles;

    @Version
    private Integer version;

    @Column(length = 100)
    @Size(min = 0, max = 100)
    @XmlTransient
    private String password;

    @Size(min = 0, max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(min = 0, max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 0, max = 100)
    @Column(length = 100)
    private String email;

    public User() {

    }

    /**
     * Constructor for security based things
     *
     * @param login    the login
     * @param password the password
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Constructor with ID only
     *
     * @param id      the id
     * @param version the version
     */
    public User(Long id, Integer version) {
        this.id = id;
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return !(id != null ? !id.equals(user.id) : user.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
