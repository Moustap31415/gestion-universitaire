package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etudiant.
 */
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_etudiant", nullable = false, unique = true)
    private String numeroEtudiant;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "formation_actuelle")
    private String formationActuelle;

    @NotNull
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEtudiant() {
        return this.numeroEtudiant;
    }

    public Etudiant numeroEtudiant(String numeroEtudiant) {
        this.setNumeroEtudiant(numeroEtudiant);
        return this;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Etudiant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Etudiant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getFormationActuelle() {
        return this.formationActuelle;
    }

    public Etudiant formationActuelle(String formationActuelle) {
        this.setFormationActuelle(formationActuelle);
        return this;
    }

    public void setFormationActuelle(String formationActuelle) {
        this.formationActuelle = formationActuelle;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public Etudiant utilisateurId(Long utilisateurId) {
        this.setUtilisateurId(utilisateurId);
        return this;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", numeroEtudiant='" + getNumeroEtudiant() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", formationActuelle='" + getFormationActuelle() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            "}";
    }
}
