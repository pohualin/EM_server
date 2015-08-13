package com.emmisolutions.emmimanager.model.program.hli;

import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;

/**
 * Holds results from HLI
 */
@Entity
@Table(name = "hli_search_request", schema = "program",
        indexes = {
                @Index(name = "ix_hli_search_request_search_term",
                        columnList = "search_term")
        }
)
public class HliSearchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 1024)
    @Column(name = "search_term", nullable = false)
    private String searchHash;

    @Column(name = "created_date_utc", columnDefinition = "timestamp")
    private DateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hliSearchRequest")
    private Collection<HliSearchResponse> programs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchHash() {
        return searchHash;
    }

    public void setSearchHash(String searchHash) {
        this.searchHash = searchHash;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Collection<HliSearchResponse> getPrograms() {
        return programs;
    }

    public void setPrograms(Collection<HliSearchResponse> programs) {
        this.programs = programs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HliSearchRequest that = (HliSearchRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
