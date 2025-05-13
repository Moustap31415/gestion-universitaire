package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.Inscription;
import sn.edu.ugb.student.service.dto.InscriptionDTO;

/**
 * Mapper for the entity {@link Inscription} and its DTO {@link InscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionMapper extends EntityMapper<InscriptionDTO, Inscription> {}
