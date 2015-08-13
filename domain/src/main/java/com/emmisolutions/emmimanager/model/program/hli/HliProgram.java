package com.emmisolutions.emmimanager.model.program.hli;

import com.emmisolutions.emmimanager.model.program.Program;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * An HLI program
 */
@Entity
@Table(name = "\"#hli_search_results\"")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedNativeQueries({
        @NamedNativeQuery(name = "org.hibernate.dialect.H2Dialect",
                query = "CREATE LOCAL TEMPORARY TABLE " +
                        "\"#hli_search_results\"(emmi_code INT PRIMARY KEY, weight INT) TRANSACTIONAL"),
        @NamedNativeQuery(name = "org.hibernate.dialect.PostgreSQL9Dialect",
                query = "CREATE LOCAL TEMPORARY TABLE " +
                        "\"#hli_search_results\"(emmi_code INT PRIMARY KEY, weight INT) ON COMMIT DROP"),
        @NamedNativeQuery(name = "org.hibernate.dialect.SQLServer2012Dialect",
                query = "CREATE TABLE #hli_search_results(emmi_code INT PRIMARY KEY, weight INT)"),
})
public class HliProgram implements Serializable {

    @Id
    @Column(name = "emmi_code")
    private Integer code;

    private int weight;

    @ManyToOne
    @SuppressWarnings("unused")
    @JoinColumn(name = "emmi_code", insertable = false, updatable = false)
    private Program program;

    /**
     * This is the emmi code
     *
     * @return usually a number (which is the emmi code) but sometimes a string which isn't the emmi code
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "HliProgram{" +
                "code='" + code + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HliProgram that = (HliProgram) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
