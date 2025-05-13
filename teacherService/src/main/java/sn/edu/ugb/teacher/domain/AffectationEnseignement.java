package sn.edu.ugb.teacher.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AffectationEnseignement.
 */
@Entity
@Table(name = "affectation_enseignement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationEnseignement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "annee_academique", nullable = false)
    private String anneeAcademique;

    @NotNull
    @Column(name = "enseignant_id", nullable = false)
    private Long enseignantId;

    @NotNull
    @Column(name = "cours_id", nullable = false)
    private Long coursId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AffectationEnseignement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public AffectationEnseignement anneeAcademique(String anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Long getEnseignantId() {
        return this.enseignantId;
    }

    public AffectationEnseignement enseignantId(Long enseignantId) {
        this.setEnseignantId(enseignantId);
        return this;
    }

    public void setEnseignantId(Long enseignantId) {
        this.enseignantId = enseignantId;
    }

    public Long getCoursId() {
        return this.coursId;
    }

    public AffectationEnseignement coursId(Long coursId) {
        this.setCoursId(coursId);
        return this;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationEnseignement)) {
            return false;
        }
        return getId() != null && getId().equals(((AffectationEnseignement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationEnseignement{" +
            "id=" + getId() +
            ", anneeAcademique='" + getAnneeAcademique() + "'" +
            ", enseignantId=" + getEnseignantId() +
            ", coursId=" + getCoursId() +
            "}";
    }
}
