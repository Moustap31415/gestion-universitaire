package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.edu.ugb.curriculum.domain.enumeration.NomSemestre;

@Entity
@Table(name = "semestre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Semestre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom", nullable = false)
    private NomSemestre nom;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @OneToMany(mappedBy = "semestre", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Curriculum> curricula = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semestre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomSemestre getNom() {
        return this.nom;
    }

    public Semestre nom(NomSemestre nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(NomSemestre nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Semestre dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Semestre dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Set<Curriculum> getCurricula() {
        return this.curricula;
    }

    public void setCurricula(Set<Curriculum> curricula) {
        if (this.curricula != null) {
            this.curricula.forEach(i -> i.setSemestre(null));
        }
        if (curricula != null) {
            curricula.forEach(i -> i.setSemestre(this));
        }
        this.curricula = curricula;
    }

    public Semestre curricula(Set<Curriculum> curricula) {
        this.setCurricula(curricula);
        return this;
    }

    public Semestre addCurriculum(Curriculum curriculum) {
        this.curricula.add(curriculum);
        curriculum.setSemestre(this);
        return this;
    }

    public Semestre removeCurriculum(Curriculum curriculum) {
        this.curricula.remove(curriculum);
        curriculum.setSemestre(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semestre)) {
            return false;
        }
        return getId() != null && getId().equals(((Semestre) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Semestre{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
