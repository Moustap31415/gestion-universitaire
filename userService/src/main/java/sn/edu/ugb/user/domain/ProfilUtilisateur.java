package sn.edu.ugb.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;

/**
 * A ProfilUtilisateur.
 */
@Entity
@Table(name = "profil_utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Entité représentant le profil d'un utilisateur")
public class ProfilUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique du profil utilisateur", example = "1")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    @Schema(description = "Nom de famille de l'utilisateur", requiredMode = Schema.RequiredMode.REQUIRED, example = "DIOP")
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    @Schema(description = "Prénom de l'utilisateur", requiredMode = Schema.RequiredMode.REQUIRED, example = "Moussa")
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    @Schema(description = "Adresse email de l'utilisateur", requiredMode = Schema.RequiredMode.REQUIRED, example = "moussa.diop@ugb.edu.sn")
    private String email;

    @Column(name = "telephone")
    @Schema(description = "Numéro de téléphone de l'utilisateur", example = "+221781234567")
    private String telephone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @Schema(description = "Rôle associé à l'utilisateur")
    private Role role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfilUtilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public ProfilUtilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public ProfilUtilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public ProfilUtilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public ProfilUtilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ProfilUtilisateur role(Role role) {
        this.setRole(role);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfilUtilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfilUtilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilUtilisateur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
