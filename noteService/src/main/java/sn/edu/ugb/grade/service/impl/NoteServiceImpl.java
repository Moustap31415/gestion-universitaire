package sn.edu.ugb.grade.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.grade.domain.Note;
import sn.edu.ugb.grade.repository.NoteRepository;
import sn.edu.ugb.grade.service.NoteService;
import sn.edu.ugb.grade.service.dto.NoteDTO;
import sn.edu.ugb.grade.service.mapper.NoteMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.grade.domain.Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private static final Logger LOG = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        LOG.debug("Request to save Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    @Override
    public NoteDTO update(NoteDTO noteDTO) {
        LOG.debug("Request to update Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    @Override
    public Optional<NoteDTO> partialUpdate(NoteDTO noteDTO) {
        LOG.debug("Request to partially update Note : {}", noteDTO);

        return noteRepository
            .findById(noteDTO.getId())
            .map(existingNote -> {
                noteMapper.partialUpdate(existingNote, noteDTO);

                return existingNote;
            })
            .map(noteRepository::save)
            .map(noteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> findAll() {
        LOG.debug("Request to get all Notes");
        return noteRepository.findAll().stream().map(noteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NoteDTO> findOne(Long id) {
        LOG.debug("Request to get Note : {}", id);
        return noteRepository.findById(id).map(noteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
