package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.Note} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Float valeur;

    private String commentaires;

    @NotNull
    private Long etudiantId;

    @NotNull
    private Long evaluationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValeur() {
        return valeur;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteDTO)) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", commentaires='" + getCommentaires() + "'" +
            ", etudiantId=" + getEtudiantId() +
            ", evaluationId=" + getEvaluationId() +
            "}";
    }
}
