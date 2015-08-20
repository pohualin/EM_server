package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Language supported by the system.
 */
@Entity
@Audited
@Table(name = "language",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_language_rfc5646_language_tag",
                columnNames = "rfc5646_language_tag"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Language extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rfc5646_language_tag", nullable = false)
    private String languageTag;

    public Language(Long id) {
        this.id = id;
    }

    public Language() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(id, language.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return languageTag;
    }
}
