package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.Evaluation;
import sn.edu.ugb.grade.service.dto.EvaluationDTO;

/**
 * Mapper for the entity {@link Evaluation} and its DTO {@link EvaluationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvaluationMapper extends EntityMapper<EvaluationDTO, Evaluation> {}
