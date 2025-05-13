package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.HistoriqueAcademique;
import sn.edu.ugb.student.service.dto.HistoriqueAcademiqueDTO;

/**
 * Mapper for the entity {@link HistoriqueAcademique} and its DTO {@link HistoriqueAcademiqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueAcademiqueMapper extends EntityMapper<HistoriqueAcademiqueDTO, HistoriqueAcademique> {}
