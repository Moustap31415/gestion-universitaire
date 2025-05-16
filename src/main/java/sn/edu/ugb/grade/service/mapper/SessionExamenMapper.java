package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.SessionExamen;
import sn.edu.ugb.grade.service.dto.SessionExamenDTO;

/**
 * Mapper for the entity {@link SessionExamen} and its DTO {@link SessionExamenDTO}.
 */
@Mapper(componentModel = "spring")
public interface SessionExamenMapper extends EntityMapper<SessionExamenDTO, SessionExamen> {}
