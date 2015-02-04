package com.emmisolutions.emmimanager.model.user;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;

/**
 * A user represents a person who logs in to the system and uses it.
 */
@Entity
@Audited
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type",
        discriminatorType = DiscriminatorType.STRING)
@XmlRootElement(name = "user")
public abstract class User extends AbstractAuditingEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    @Column(columnDefinition = "int")
    private Integer version;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "first_name", length = 50, nullable = false, columnDefinition = "nvarchar(50)")
    private String firstName;

    private boolean active = true;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = false;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "last_name", length = 50, nullable = false, columnDefinition = "nvarchar(50)")
    private String lastName;

    @NotNull
    @Size(max = 610)
    @Column(name = "normalized_name", length = 610, nullable = false, columnDefinition = "nvarchar(610)")
    @NotAudited
    @Pattern(regexp = "[a-z0-9]*", message = "Normalized name can only contain lowercase letters and digits")
    private String normalizedName;

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
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", version=" + version +
                ", active=" + active +
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Creates the full name string (first + last)
     *
     * @return the full name or blank
     */
    public String getFullName() {
        return WordUtils.capitalizeFully(StringUtils.trimToEmpty(StringUtils.defaultString(getFirstName()) +
                " " + StringUtils.defaultString(getLastName())));
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

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
}
