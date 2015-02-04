package com.emmisolutions.emmimanager.model.audit;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.util.Date;

/**
 * Store the username/login who made each revision
 */
@Entity
@Table(name = "revision_info")
@RevisionEntity(UsernameRevisionListener.class)
public class UsernameRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "revision")
    private long id;

    @RevisionTimestamp
    private long timestamp;

    @Size(min = 0, max = 255)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsernameRevisionEntity that = (UsernameRevisionEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "UsernameRevisionEntity{" +
                "id=" + id +
                ", revisionDate=" + DateFormat.getDateTimeInstance().format(new Date(timestamp)) +
                ", username='" + username + '\'' +
                '}';
    }
}
