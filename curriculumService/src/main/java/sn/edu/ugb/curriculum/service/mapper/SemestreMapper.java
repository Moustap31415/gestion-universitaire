package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Semestre;
import sn.edu.ugb.curriculum.service.dto.SemestreDTO;

/**
 * Mapper for the entity {@link Semestre} and its DTO {@link SemestreDTO}.
 */
@Mapper(componentModel = "spring")
public interface SemestreMapper extends EntityMapper<SemestreDTO, Semestre> {}
