package sn.edu.ugb.grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.edu.ugb.grade.domain.enumeration.TypeEvaluation;

/**
 * A Evaluation.
 */
@Entity
@Table(name = "evaluation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEvaluation type;

    @NotNull
    @Column(name = "note_maximale", nullable = false)
    private Float noteMaximale;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "cours_id", nullable = false)
    private Long coursId;

    @NotNull
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeEvaluation getType() {
        return this.type;
    }

    public Evaluation type(TypeEvaluation type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeEvaluation type) {
        this.type = type;
    }

    public Float getNoteMaximale() {
        return this.noteMaximale;
    }

    public Evaluation noteMaximale(Float noteMaximale) {
        this.setNoteMaximale(noteMaximale);
        return this;
    }

    public void setNoteMaximale(Float noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Evaluation date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCoursId() {
        return this.coursId;
    }

    public Evaluation coursId(Long coursId) {
        this.setCoursId(coursId);
        return this;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public Long getSessionId() {
        return this.sessionId;
    }

    public Evaluation sessionId(Long sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaluation)) {
            return false;
        }
        return getId() != null && getId().equals(((Evaluation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", noteMaximale=" + getNoteMaximale() +
            ", date='" + getDate() + "'" +
            ", coursId=" + getCoursId() +
            ", sessionId=" + getSessionId() +
            "}";
    }
}
