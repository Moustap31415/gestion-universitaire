package sn.edu.ugb.grade.service;

import java.util.List;
import java.util.Optional;
import sn.edu.ugb.grade.service.dto.NoteDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param noteDTO the entity to save.
     * @return the persisted entity.
     */
    NoteDTO save(NoteDTO noteDTO);

    /**
     * Updates a note.
     *
     * @param noteDTO the entity to update.
     * @return the persisted entity.
     */
    NoteDTO update(NoteDTO noteDTO);

    /**
     * Partially updates a note.
     *
     * @param noteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NoteDTO> partialUpdate(NoteDTO noteDTO);

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    List<NoteDTO> findAll();

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NoteDTO> findOne(Long id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
