package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.Etudiant;
import sn.edu.ugb.student.service.dto.EtudiantDTO;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {}
