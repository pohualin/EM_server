package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by achavan on 6/19/2015.
 */
@Entity
@Audited
@Table(name = "client_team_self_reg_config")
public class ClientTeamSelfRegConfiguration extends AbstractAuditingEntity {

    public ClientTeamSelfRegConfiguration() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_team_id", nullable = false)
    private Team team;

    @NotNull
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    @Pattern(regexp = "[\\-A-Za-z0-9_]*", message = "Name can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( ) /")
    private String code;

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientTeamSelfRegConfiguration that = (ClientTeamSelfRegConfiguration) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientTeamSelfRegConfiguration{" +
                "id=" + id +
                ", version=" + version +
                ", team=" + team +
                ", code='" + code + '\'' +
                '}';
    }
}
