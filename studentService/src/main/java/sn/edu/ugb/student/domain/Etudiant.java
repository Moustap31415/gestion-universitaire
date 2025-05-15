package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "etudiant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String numeroEtudiant;

    private LocalDate dateNaissance;
    private String adresse;
    private String formationActuelle;

    @NotNull
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HistoriqueAcademique> historiques = new HashSet<>();

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEtudiant() {
        return this.numeroEtudiant;
    }

    public Etudiant numeroEtudiant(String numeroEtudiant) {
        this.setNumeroEtudiant(numeroEtudiant);
        return this;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Etudiant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Etudiant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getFormationActuelle() {
        return this.formationActuelle;
    }

    public Etudiant formationActuelle(String formationActuelle) {
        this.setFormationActuelle(formationActuelle);
        return this;
    }

    public void setFormationActuelle(String formationActuelle) {
        this.formationActuelle = formationActuelle;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public Etudiant utilisateurId(Long utilisateurId) {
        this.setUtilisateurId(utilisateurId);
        return this;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Set<HistoriqueAcademique> getHistoriques() {
        return this.historiques;
    }

    public void setHistoriques(Set<HistoriqueAcademique> historiques) {
        if (this.historiques != null) {
            this.historiques.forEach(i -> i.setEtudiant(null));
        }
        if (historiques != null) {
            historiques.forEach(i -> i.setEtudiant(this));
        }
        this.historiques = historiques;
    }

    public Etudiant historiques(Set<HistoriqueAcademique> historiques) {
        this.setHistoriques(historiques);
        return this;
    }

    public Etudiant addHistorique(HistoriqueAcademique historique) {
        this.historiques.add(historique);
        historique.setEtudiant(this);
        return this;
    }

    public Etudiant removeHistorique(HistoriqueAcademique historique) {
        this.historiques.remove(historique);
        historique.setEtudiant(null);
        return this;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setEtudiant(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setEtudiant(this));
        }
        this.inscriptions = inscriptions;
    }

    public Etudiant inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Etudiant addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setEtudiant(this);
        return this;
    }

    public Etudiant removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setEtudiant(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", numeroEtudiant='" + getNumeroEtudiant() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", formationActuelle='" + getFormationActuelle() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            "}";
    }
}
