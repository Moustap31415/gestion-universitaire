package sn.edu.ugb.teacher.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.teacher.domain.Enseignant;
import sn.edu.ugb.teacher.service.dto.EnseignantDTO;

/**
 * Mapper for the entity {@link Enseignant} and its DTO {@link EnseignantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnseignantMapper extends EntityMapper<EnseignantDTO, Enseignant> {}
