package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Filiere;
import sn.edu.ugb.curriculum.service.dto.FiliereDTO;

/**
 * Mapper for the entity {@link Filiere} and its DTO {@link FiliereDTO}.
 */
@Mapper(componentModel = "spring")
public interface FiliereMapper extends EntityMapper<FiliereDTO, Filiere> {}
