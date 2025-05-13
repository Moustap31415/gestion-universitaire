package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Matiere;
import sn.edu.ugb.curriculum.service.dto.MatiereDTO;

/**
 * Mapper for the entity {@link Matiere} and its DTO {@link MatiereDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatiereMapper extends EntityMapper<MatiereDTO, Matiere> {}
