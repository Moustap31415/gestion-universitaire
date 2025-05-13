package sn.edu.ugb.teacher.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_enseignant", nullable = false, unique = true)
    private String numeroEnseignant;

    @Column(name = "specialisation")
    private String specialisation;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @NotNull
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enseignant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEnseignant() {
        return this.numeroEnseignant;
    }

    public Enseignant numeroEnseignant(String numeroEnseignant) {
        this.setNumeroEnseignant(numeroEnseignant);
        return this;
    }

    public void setNumeroEnseignant(String numeroEnseignant) {
        this.numeroEnseignant = numeroEnseignant;
    }

    public String getSpecialisation() {
        return this.specialisation;
    }

    public Enseignant specialisation(String specialisation) {
        this.setSpecialisation(specialisation);
        return this;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public LocalDate getDateEmbauche() {
        return this.dateEmbauche;
    }

    public Enseignant dateEmbauche(LocalDate dateEmbauche) {
        this.setDateEmbauche(dateEmbauche);
        return this;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public Enseignant utilisateurId(Long utilisateurId) {
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
        if (!(o instanceof Enseignant)) {
            return false;
        }
        return getId() != null && getId().equals(((Enseignant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + getId() +
            ", numeroEnseignant='" + getNumeroEnseignant() + "'" +
            ", specialisation='" + getSpecialisation() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            "}";
    }
}
