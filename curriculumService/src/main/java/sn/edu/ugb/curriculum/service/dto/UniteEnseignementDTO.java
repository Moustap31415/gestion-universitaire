package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.UniteEnseignement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniteEnseignementDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String code;

    @NotNull
    private Long filiereId;

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

    public Long getFiliereId() {
        return filiereId;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniteEnseignementDTO)) {
            return false;
        }

        UniteEnseignementDTO uniteEnseignementDTO = (UniteEnseignementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniteEnseignementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniteEnseignementDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            ", filiereId=" + getFiliereId() +
            "}";
    }
}
