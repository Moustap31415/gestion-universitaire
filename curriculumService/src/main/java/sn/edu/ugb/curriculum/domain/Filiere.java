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
 * Entité représentant une filière de formation
 */
@Entity
@Table(name = "filiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Filière de formation proposée par l'établissement")
public class Filiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique de la filière", example = "1")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    @Schema(description = "Nom complet de la filière", requiredMode = Schema.RequiredMode.REQUIRED, example = "Licence Informatique")
    private String nom;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    @Schema(description = "Code unique de la filière", requiredMode = Schema.RequiredMode.REQUIRED, example = "INFO-LIC")
    private String code;

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Schema(description = "Unités d'enseignement de la filière")
    private Set<UniteEnseignement> unitesEnseignement = new HashSet<>();

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Schema(description = "Curricula associés à la filière")
    private Set<Curriculum> curricula = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Filiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Filiere nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return this.code;
    }

    public Filiere code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<UniteEnseignement> getUnitesEnseignement() {
        return this.unitesEnseignement;
    }

    public void setUnitesEnseignement(Set<UniteEnseignement> unitesEnseignement) {
        if (this.unitesEnseignement != null) {
            this.unitesEnseignement.forEach(i -> i.setFiliere(null));
        }
        if (unitesEnseignement != null) {
            unitesEnseignement.forEach(i -> i.setFiliere(this));
        }
        this.unitesEnseignement = unitesEnseignement;
    }

    public Filiere unitesEnseignement(Set<UniteEnseignement> unitesEnseignement) {
        this.setUnitesEnseignement(unitesEnseignement);
        return this;
    }

    public Filiere addUniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.unitesEnseignement.add(uniteEnseignement);
        uniteEnseignement.setFiliere(this);
        return this;
    }

    public Filiere removeUniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.unitesEnseignement.remove(uniteEnseignement);
        uniteEnseignement.setFiliere(null);
        return this;
    }

    public Set<Curriculum> getCurricula() {
        return this.curricula;
    }

    public void setCurricula(Set<Curriculum> curricula) {
        if (this.curricula != null) {
            this.curricula.forEach(i -> i.setFiliere(null));
        }
        if (curricula != null) {
            curricula.forEach(i -> i.setFiliere(this));
        }
        this.curricula = curricula;
    }

    public Filiere curricula(Set<Curriculum> curricula) {
        this.setCurricula(curricula);
        return this;
    }

    public Filiere addCurriculum(Curriculum curriculum) {
        this.curricula.add(curriculum);
        curriculum.setFiliere(this);
        return this;
    }

    public Filiere removeCurriculum(Curriculum curriculum) {
        this.curricula.remove(curriculum);
        curriculum.setFiliere(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filiere)) {
            return false;
        }
        return getId() != null && getId().equals(((Filiere) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Filiere{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
