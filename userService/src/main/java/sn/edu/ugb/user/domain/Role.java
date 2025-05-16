package sn.edu.ugb.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Schema(description = "Entité représentant un rôle utilisateur dans le système")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Schema(description = "ID unique du rôle", example = "1")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    @Schema(description = "Nom du rôle", requiredMode = Schema.RequiredMode.REQUIRED,
        example = "ROLE_ADMIN", allowableValues = {"ROLE_ADMIN", "ROLE_ENSEIGNANT", "ROLE_ETUDIANT"})
    private String nom;

    @Column(name = "description")
    @Schema(description = "Description du rôle", example = "Administrateur du système")
    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Schema(description = "Liste des profils utilisateurs associés à ce rôle")
    private Set<ProfilUtilisateur> profilUtilisateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Role nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Role description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProfilUtilisateur> getProfilUtilisateurs() {
        return this.profilUtilisateurs;
    }

    public void setProfilUtilisateurs(Set<ProfilUtilisateur> profilUtilisateurs) {
        if (this.profilUtilisateurs != null) {
            this.profilUtilisateurs.forEach(i -> i.setRole(null));
        }
        if (profilUtilisateurs != null) {
            profilUtilisateurs.forEach(i -> i.setRole(this));
        }
        this.profilUtilisateurs = profilUtilisateurs;
    }

    public Role profilUtilisateurs(Set<ProfilUtilisateur> profilUtilisateurs) {
        this.setProfilUtilisateurs(profilUtilisateurs);
        return this;
    }

    public Role addProfilUtilisateur(ProfilUtilisateur profilUtilisateur) {
        this.profilUtilisateurs.add(profilUtilisateur);
        profilUtilisateur.setRole(this);
        return this;
    }

    public Role removeProfilUtilisateur(ProfilUtilisateur profilUtilisateur) {
        this.profilUtilisateurs.remove(profilUtilisateur);
        profilUtilisateur.setRole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return getId() != null && getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
