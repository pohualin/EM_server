package com.emmisolutions.emmimanager.model.program;

import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * An Emmi Program
 */
@Entity
@Table(name = "rf_emmi", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class Program {

    @Id
    @Column(name = "emmi_cd")
    private Integer id;

    private boolean active;

    @Column(name = "emmi_nm_mxd")
    private String name;

    @Column(name = "emmi_descrpn")
    private String description;

    @Column(name = "emmi_flnm")
    private String fileName;

    @Column(name = "overlay_flnm")
    private String overlayFileName;

    @ManyToOne
    @JoinColumn(name = "emmi_brnd_cd")
    private Brand brand;

    @Column(name = "last_trckd_sctn")
    private Integer lastTrackedSection;

    private Boolean shell;

    private boolean available;

    @Column(name = "tpc_id")
    private String tpcId;

    @ManyToOne
    @JoinColumn(name = "emmi_tp_cd")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "emmi_src_cd")
    private Source source;

    @OneToMany(mappedBy = "program")
    private Set<ProgramSpecialty> programSpecialty;

    @Column(name = "release_dt")
    private LocalDateTime releaseDate;

    @NotAudited
    @XmlTransient
    @SuppressWarnings("unused")
    @OneToMany(mappedBy = "program")
    private Set<HliSearchResponse> hliProgram; // don't give this field a getter and setter ever

    public Program() {
    }

    /**
     * Construct a Program with its id
     *
     * @param id the id
     */
    public Program(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Integer getLastTrackedSection() {
        return lastTrackedSection;
    }

    public void setLastTrackedSection(Integer lastTrackedSection) {
        this.lastTrackedSection = lastTrackedSection;
    }

    public Boolean isShell() {
        return shell;
    }

    public void setShell(Boolean shell) {
        this.shell = shell;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTpcId() {
        return tpcId;
    }

    public void setTpcId(String tpcId) {
        this.tpcId = tpcId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }


    public Set<ProgramSpecialty> getProgramSpecialty() {
        return programSpecialty;
    }

    public void setProgramSpecialty(Set<ProgramSpecialty> programSpecialty) {
        this.programSpecialty = programSpecialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(id, program.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

}
