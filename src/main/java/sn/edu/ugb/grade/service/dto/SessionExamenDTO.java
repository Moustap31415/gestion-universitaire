package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.SessionExamen} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionExamenDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionExamenDTO)) {
            return false;
        }

        SessionExamenDTO sessionExamenDTO = (SessionExamenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionExamenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionExamenDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
