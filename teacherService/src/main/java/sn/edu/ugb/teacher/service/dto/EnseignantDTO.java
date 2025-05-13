package sn.edu.ugb.teacher.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.teacher.domain.Enseignant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnseignantDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroEnseignant;

    private String specialisation;

    private LocalDate dateEmbauche;

    @NotNull
    private Long utilisateurId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEnseignant() {
        return numeroEnseignant;
    }

    public void setNumeroEnseignant(String numeroEnseignant) {
        this.numeroEnseignant = numeroEnseignant;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnseignantDTO)) {
            return false;
        }

        EnseignantDTO enseignantDTO = (EnseignantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enseignantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnseignantDTO{" +
            "id=" + getId() +
            ", numeroEnseignant='" + getNumeroEnseignant() + "'" +
            ", specialisation='" + getSpecialisation() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            "}";
    }
}
