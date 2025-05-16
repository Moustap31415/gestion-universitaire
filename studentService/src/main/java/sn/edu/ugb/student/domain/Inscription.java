package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entité représentant une inscription d'étudiant
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Entité représentant une inscription d'étudiant à une filière et un semestre")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique de l'inscription", example = "1")
    private Long id;

    @NotNull
    @Column(name = "en_cours", nullable = false)
    @Schema(description = "Indique si l'inscription est en cours", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean enCours;

    @ManyToOne(optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @Schema(description = "Étudiant associé à l'inscription")
    private Etudiant etudiant;

    @NotNull
    @Column(name = "filiere_id", nullable = false)
    @Schema(description = "ID de la filière", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long filiereId;

    @NotNull
    @Column(name = "semestre_id", nullable = false)
    @Schema(description = "ID du semestre", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
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

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public Inscription etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
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

    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", enCours='" + getEnCours() + "'" +
            ", filiereId=" + getFiliereId() +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
