package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import sn.edu.ugb.curriculum.domain.enumeration.NomSemestre;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.Semestre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SemestreDTO implements Serializable {

    private Long id;

    @NotNull
    private NomSemestre nom;

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

    public NomSemestre getNom() {
        return nom;
    }

    public void setNom(NomSemestre nom) {
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
        if (!(o instanceof SemestreDTO)) {
            return false;
        }

        SemestreDTO semestreDTO = (SemestreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, semestreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemestreDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
