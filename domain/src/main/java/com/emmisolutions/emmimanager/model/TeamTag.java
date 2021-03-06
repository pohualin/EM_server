package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A tag on a team
 */
@Audited
@Entity
@Table(name = "client_team_tag")
@XmlRootElement(name = "client_team_tag")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamTag extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    /**
     * Default constructor
     */
    public TeamTag() {
    }

    /**
     * Constructor with id
     * 
     * @param id
     *            to use
     */
    public TeamTag(long id) {
        this.id = id;
    }

    /**
     * @param team to associate with
     * @param tag  to associate with
     */
    public TeamTag(Team team, Tag tag) {
        setTeam(team);
        setTag(tag);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamTag teamTag = (TeamTag) o;

        if (id != null ? !id.equals(teamTag.id) : teamTag.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
