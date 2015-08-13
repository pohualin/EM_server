package com.emmisolutions.emmimanager.model.program.hli;

import com.emmisolutions.emmimanager.model.program.Program;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * An HLI search response
 */
@Entity
@Table(name = "hli_search_response", schema = "program",
        indexes = {
                @Index(name = "ix_hli_search_response_hli_search_request_id_emmi_code",
                        columnList = "hli_search_request_id, emmi_code")
        }
)
public class HliSearchResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int weight;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hli_search_request_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hli_search_response_hli_search_request"))
    private HliSearchRequest hliSearchRequest;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "emmi_code", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hli_search_response_program"))
    private Program program;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public HliSearchRequest getHliSearchRequest() {
        return hliSearchRequest;
    }

    public void setHliSearchRequest(HliSearchRequest hliSearchRequest) {
        this.hliSearchRequest = hliSearchRequest;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HliSearchResponse that = (HliSearchResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
