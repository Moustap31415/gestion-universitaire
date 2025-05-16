package sn.edu.ugb.grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private Float valeur;

    @Column(name = "commentaires")
    private String commentaires;

    @NotNull
    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @NotNull
    @Column(name = "evaluation_id", nullable = false)
    private Long evaluationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValeur() {
        return this.valeur;
    }

    public Note valeur(Float valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public String getCommentaires() {
        return this.commentaires;
    }

    public Note commentaires(String commentaires) {
        this.setCommentaires(commentaires);
        return this;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Long getEtudiantId() {
        return this.etudiantId;
    }

    public Note etudiantId(Long etudiantId) {
        this.setEtudiantId(etudiantId);
        return this;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getEvaluationId() {
        return this.evaluationId;
    }

    public Note evaluationId(Long evaluationId) {
        this.setEvaluationId(evaluationId);
        return this;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return getId() != null && getId().equals(((Note) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", commentaires='" + getCommentaires() + "'" +
            ", etudiantId=" + getEtudiantId() +
            ", evaluationId=" + getEvaluationId() +
            "}";
    }
}
