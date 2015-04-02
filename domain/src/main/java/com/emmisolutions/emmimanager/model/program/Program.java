package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.*;

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
}
