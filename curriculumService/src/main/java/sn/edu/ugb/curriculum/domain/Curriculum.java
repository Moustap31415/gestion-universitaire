package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Curriculum.
 */
@Entity
@Table(name = "curriculum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Curriculum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "filiere_id", nullable = false)
    private Long filiereId;

    @NotNull
    @Column(name = "module_id", nullable = false)
    private Long moduleId;

    @NotNull
    @Column(name = "semestre_id", nullable = false)
    private Long semestreId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curriculum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiliereId() {
        return this.filiereId;
    }

    public Curriculum filiereId(Long filiereId) {
        this.setFiliereId(filiereId);
        return this;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public Curriculum moduleId(Long moduleId) {
        this.setModuleId(moduleId);
        return this;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getSemestreId() {
        return this.semestreId;
    }

    public Curriculum semestreId(Long semestreId) {
        this.setSemestreId(semestreId);
        return this;
    }

    public void setSemestreId(Long semestreId) {
        this.semestreId = semestreId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curriculum)) {
            return false;
        }
        return getId() != null && getId().equals(((Curriculum) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curriculum{" +
            "id=" + getId() +
            ", filiereId=" + getFiliereId() +
            ", moduleId=" + getModuleId() +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
