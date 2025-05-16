package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entité représentant une matière/cours
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Matière ou cours enseigné dans une unité d'enseignement")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique de la matière", example = "1")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    @Schema(description = "Nom de la matière", requiredMode = Schema.RequiredMode.REQUIRED, example = "Algorithmique")
    private String nom;

    @NotNull
    @Column(name = "heures", nullable = false)
    @Schema(description = "Nombre d'heures de cours", requiredMode = Schema.RequiredMode.REQUIRED, example = "60")
    private Integer heures;

    @NotNull
    @Column(name = "credits", nullable = false)
    @Schema(description = "Nombre de crédits ECTS", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Integer credits;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unite_enseignement_id", nullable = false)
    @Schema(description = "Unité d'enseignement à laquelle la matière appartient")
    private UniteEnseignement uniteEnseignement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Matiere nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getHeures() {
        return this.heures;
    }

    public Matiere heures(Integer heures) {
        this.setHeures(heures);
        return this;
    }

    public void setHeures(Integer heures) {
        this.heures = heures;
    }

    public Integer getCredits() {
        return this.credits;
    }

    public Matiere credits(Integer credits) {
        this.setCredits(credits);
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public UniteEnseignement getUniteEnseignement() {
        return this.uniteEnseignement;
    }

    public void setUniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.uniteEnseignement = uniteEnseignement;
    }

    public Matiere uniteEnseignement(UniteEnseignement uniteEnseignement) {
        this.setUniteEnseignement(uniteEnseignement);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return getId() != null && getId().equals(((Matiere) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", heures=" + getHeures() +
            ", credits=" + getCredits() +
            "}";
    }
}
