package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.Note;
import sn.edu.ugb.grade.service.dto.NoteDTO;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {}
