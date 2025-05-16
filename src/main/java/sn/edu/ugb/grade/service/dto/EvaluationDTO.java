package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import sn.edu.ugb.grade.domain.enumeration.TypeEvaluation;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.Evaluation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeEvaluation type;

    @NotNull
    private Float noteMaximale;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long coursId;

    @NotNull
    private Long sessionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeEvaluation getType() {
        return type;
    }

    public void setType(TypeEvaluation type) {
        this.type = type;
    }

    public Float getNoteMaximale() {
        return noteMaximale;
    }

    public void setNoteMaximale(Float noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationDTO)) {
            return false;
        }

        EvaluationDTO evaluationDTO = (EvaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", noteMaximale=" + getNoteMaximale() +
            ", date='" + getDate() + "'" +
            ", coursId=" + getCoursId() +
            ", sessionId=" + getSessionId() +
            "}";
    }
}
