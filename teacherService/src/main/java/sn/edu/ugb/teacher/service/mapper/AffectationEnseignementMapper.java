package sn.edu.ugb.teacher.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.teacher.domain.AffectationEnseignement;
import sn.edu.ugb.teacher.service.dto.AffectationEnseignementDTO;

/**
 * Mapper for the entity {@link AffectationEnseignement} and its DTO {@link AffectationEnseignementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationEnseignementMapper extends EntityMapper<AffectationEnseignementDTO, AffectationEnseignement> {}
