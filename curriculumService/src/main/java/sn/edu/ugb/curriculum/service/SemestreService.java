package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.SemestreDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.Semestre}.
 */
public interface SemestreService {
    /**
     * Save a semestre.
     *
     * @param semestreDTO the entity to save.
     * @return the persisted entity.
     */
    SemestreDTO save(SemestreDTO semestreDTO);

    /**
     * Updates a semestre.
     *
     * @param semestreDTO the entity to update.
     * @return the persisted entity.
     */
    SemestreDTO update(SemestreDTO semestreDTO);

    /**
     * Partially updates a semestre.
     *
     * @param semestreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SemestreDTO> partialUpdate(SemestreDTO semestreDTO);

    /**
     * Get all the semestres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SemestreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" semestre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SemestreDTO> findOne(Long id);

    /**
     * Delete the "id" semestre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
