package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.Curriculum} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurriculumDTO implements Serializable {

    private Long id;

    @NotNull
    private Long filiereId;

    @NotNull
    private Long moduleId;

    @NotNull
    private Long semestreId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiliereId() {
        return filiereId;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getSemestreId() {
        return semestreId;
    }

    public void setSemestreId(Long semestreId) {
        this.semestreId = semestreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurriculumDTO)) {
            return false;
        }

        CurriculumDTO curriculumDTO = (CurriculumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, curriculumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurriculumDTO{" +
            "id=" + getId() +
            ", filiereId=" + getFiliereId() +
            ", moduleId=" + getModuleId() +
            ", semestreId=" + getSemestreId() +
            "}";
    }
}
