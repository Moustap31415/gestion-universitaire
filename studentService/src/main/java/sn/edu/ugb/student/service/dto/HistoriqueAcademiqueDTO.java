package sn.edu.ugb.student.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import sn.edu.ugb.student.domain.enumeration.StatutAcademique;

/**
 * A DTO for the {@link sn.edu.ugb.student.domain.HistoriqueAcademique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueAcademiqueDTO implements Serializable {

    private Long id;

    @NotNull
    private StatutAcademique statut;

    @NotNull
    private Instant dateInscription;

    @NotNull
    private Long etudiantId;

    @NotNull
    private Long semestreId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutAcademique getStatut() {
        return statut;
    }

    public void setStatut(StatutAcademique statut) {
        this.statut = statut;
    }

    public Instant getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getSemestreId() {
        return semestreId;
    }

    public void setSemestreId(Long semestreId) {
        this.semestreId = semestreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueAcademiqueDTO)) {
            return false;
        }

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = (HistoriqueAcademiqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueAcademiqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueAcademiqueDTO{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", etudiantId=" + getEtudiantId() +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
