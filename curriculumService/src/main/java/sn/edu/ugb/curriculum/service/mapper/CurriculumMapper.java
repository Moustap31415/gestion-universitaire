package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Curriculum;
import sn.edu.ugb.curriculum.service.dto.CurriculumDTO;

/**
 * Mapper for the entity {@link Curriculum} and its DTO {@link CurriculumDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurriculumMapper extends EntityMapper<CurriculumDTO, Curriculum> {}
