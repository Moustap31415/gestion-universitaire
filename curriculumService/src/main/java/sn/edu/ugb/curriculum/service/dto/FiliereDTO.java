package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.Filiere} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiliereDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiliereDTO)) {
            return false;
        }

        FiliereDTO filiereDTO = (FiliereDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filiereDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiliereDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
