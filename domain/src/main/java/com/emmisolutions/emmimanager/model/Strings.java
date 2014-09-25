package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Stores externalized strings by language tag
 */
@Entity
@Audited
@Table(name = "strings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_strings_language_id_key",
                columnNames = {"language_id", "key"}))
public class Strings extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_language_language_id"))
    private Language language;

    @NotNull
    @Size(max = 1024)
    @Column(name = "key", nullable = false)
    private String key;

    @Size(max = 2048)
    private String value;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Strings strings = (Strings) o;
        return !(id != null ? !id.equals(strings.id) : strings.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
