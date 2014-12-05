package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 * A client tag within a group.
 */
@Audited
@Entity
@Table(name = "client_group_tag")
@XmlRootElement(name = "client_group_tag")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tag extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tag")
    @XmlTransient
    private Set<TeamTag> teamTags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return !(id != null ? !id.equals(tag.id) : tag.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public Set<TeamTag> getTeamTags() {
        return teamTags;
    }

    public void setTeamTags(Set<TeamTag> teamTags) {
        this.teamTags = teamTags;
    }
}
