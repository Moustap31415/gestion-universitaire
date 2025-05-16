package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entité représentant une unité d'enseignement (UE)
 */
@Entity
@Table(name = "unite_enseignement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Unité d'enseignement (UE) regroupant plusieurs matières")
public class UniteEnseignement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique de l'unité d'enseignement", example = "1")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    @Schema(description = "Nom complet de l'UE", requiredMode = Schema.RequiredMode.REQUIRED, example = "Fondamentaux de l'informatique")
    private String nom;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    @Schema(description = "Code unique de l'UE", requiredMode = Schema.RequiredMode.REQUIRED, example = "UE-INFO-101")
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "filiere_id", nullable = false)
    @Schema(description = "Filière à laquelle appartient l'UE")
    private Filiere filiere;

    @OneToMany(mappedBy = "uniteEnseignement", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Schema(description = "Matières composant cette UE")
    private Set<Matiere> matieres = new HashSet<>();

    @OneToMany(mappedBy = "uniteEnseignement", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Schema(description = "Curricula associés à cette UE")
    private Set<Curriculum> curricula = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UniteEnseignement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public UniteEnseignement nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return this.code;
    }

    public UniteEnseignement code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public UniteEnseignement filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        if (this.matieres != null) {
            this.matieres.forEach(i -> i.setUniteEnseignement(null));
        }
        if (matieres != null) {
            matieres.forEach(i -> i.setUniteEnseignement(this));
        }
        this.matieres = matieres;
    }

    public UniteEnseignement matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public UniteEnseignement addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.setUniteEnseignement(this);
        return this;
    }

    public UniteEnseignement removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.setUniteEnseignement(null);
        return this;
    }

    public Set<Curriculum> getCurricula() {
        return this.curricula;
    }

    public void setCurricula(Set<Curriculum> curricula) {
        if (this.curricula != null) {
            this.curricula.forEach(i -> i.setUniteEnseignement(null));
        }
        if (curricula != null) {
            curricula.forEach(i -> i.setUniteEnseignement(this));
        }
        this.curricula = curricula;
    }

    public UniteEnseignement curricula(Set<Curriculum> curricula) {
        this.setCurricula(curricula);
        return this;
    }

    public UniteEnseignement addCurriculum(Curriculum curriculum) {
        this.curricula.add(curriculum);
        curriculum.setUniteEnseignement(this);
        return this;
    }

    public UniteEnseignement removeCurriculum(Curriculum curriculum) {
        this.curricula.remove(curriculum);
        curriculum.setUniteEnseignement(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniteEnseignement)) {
            return false;
        }
        return getId() != null && getId().equals(((UniteEnseignement) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UniteEnseignement{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
