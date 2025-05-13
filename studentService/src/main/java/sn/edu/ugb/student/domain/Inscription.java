package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "en_cours", nullable = false)
    private Boolean enCours;

    @NotNull
    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @NotNull
    @Column(name = "filiere_id", nullable = false)
    private Long filiereId;

    @NotNull
    @Column(name = "semestre_id", nullable = false)
    private Long semestreId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnCours() {
        return this.enCours;
    }

    public Inscription enCours(Boolean enCours) {
        this.setEnCours(enCours);
        return this;
    }

    public void setEnCours(Boolean enCours) {
        this.enCours = enCours;
    }

    public Long getEtudiantId() {
        return this.etudiantId;
    }

    public Inscription etudiantId(Long etudiantId) {
        this.setEtudiantId(etudiantId);
        return this;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getFiliereId() {
        return this.filiereId;
    }

    public Inscription filiereId(Long filiereId) {
        this.setFiliereId(filiereId);
        return this;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    public Long getSemestreId() {
        return this.semestreId;
    }

    public Inscription semestreId(Long semestreId) {
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
        if (!(o instanceof Inscription)) {
            return false;
        }
        return getId() != null && getId().equals(((Inscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", enCours='" + getEnCours() + "'" +
            ", etudiantId=" + getEtudiantId() +
            ", filiereId=" + getFiliereId() +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
