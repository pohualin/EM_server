package com.emmisolutions.emmimanager.model.audit;

import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Store the username/login who made each revision
 */
@Entity
@Table(name = "revision_info", schema = "audit")
@RevisionEntity(UsernameRevisionListener.class)
public class UsernameRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "revision")
    private long id;

    @RevisionTimestamp
    private long timestamp;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false, columnDefinition = "bigint", nullable = false)
    private User user;

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
                ", user='" + user + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
