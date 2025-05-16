package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.edu.ugb.student.domain.enumeration.StatutAcademique;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entité représentant l'historique académique d'un étudiant
 */
@Entity
@Table(name = "historique_academique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Entité représentant l'historique académique d'un étudiant pour un semestre")
public class HistoriqueAcademique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique de l'historique académique", example = "1")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    @Schema(description = "Statut académique de l'étudiant",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "EN_COURS",
        allowableValues = {"EN_COURS", "VALIDE", "ABANDON"})
    private StatutAcademique statut;

    @NotNull
    @Column(name = "date_inscription", nullable = false)
    @Schema(description = "Date d'inscription", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-09-01T00:00:00Z")
    private Instant dateInscription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @Schema(description = "Étudiant associé à l'historique académique")
    private Etudiant etudiant;

    @NotNull
    @Column(name = "semestre_id", nullable = false)
    @Schema(description = "ID du semestre", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long semestreId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueAcademique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutAcademique getStatut() {
        return this.statut;
    }

    public HistoriqueAcademique statut(StatutAcademique statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutAcademique statut) {
        this.statut = statut;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public HistoriqueAcademique dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public HistoriqueAcademique etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Long getSemestreId() {
        return this.semestreId;
    }

    public HistoriqueAcademique semestreId(Long semestreId) {
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
        if (!(o instanceof HistoriqueAcademique)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueAcademique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "HistoriqueAcademique{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
