package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entité représentant un curriculum (association entre filière, UE et semestre)
 */
@Entity
@Table(name = "curriculum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Association entre une filière, une unité d'enseignement et un semestre")
public class Curriculum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique du curriculum", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "filiere_id", nullable = false)
    @Schema(description = "Filière associée au curriculum")
    private Filiere filiere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unite_enseignement_id", nullable = false)
    @Schema(description = "Unité d'enseignement associée au curriculum")
    private UniteEnseignement uniteEnseignement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semestre_id", nullable = false)
    @Schema(description = "Semestre associé au curriculum")
    private Semestre semestre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curriculum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Curriculum filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public UniteEnseignement getUniteEnseignement() {
        return this.uniteEnseignement;
    }

    public void setUniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.uniteEnseignement = uniteEnseignement;
    }

    public Curriculum uniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.setUniteEnseignement(uniteEnseignement);
        return this;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Curriculum semestre(Semestre semestre) {
        this.setSemestre(semestre);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curriculum)) {
            return false;
        }
        return getId() != null && getId().equals(((Curriculum) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Curriculum{" +
            "id=" + getId() +
            "}";
    }
}
