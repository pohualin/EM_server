package com.emmisolutions.emmimanager.model.audit.logout;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A logout source.. the trigger for the logout
 */
@Entity
@Table(name = "users_logout_source")
@XmlAccessorType(XmlAccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LogoutSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    private LogoutSourceName name;

    public LogoutSource() {
    }

    /**
     * LogoutSource by name
     *
     * @param name to use
     */
    public LogoutSource(LogoutSourceName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogoutSourceName getName() {
        return name;
    }

    public void setName(LogoutSourceName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogoutSource that = (LogoutSource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LogoutSource{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
