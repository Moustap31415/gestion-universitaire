package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.CurriculumDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.Curriculum}.
 */
public interface CurriculumService {
    /**
     * Save a curriculum.
     *
     * @param curriculumDTO the entity to save.
     * @return the persisted entity.
     */
    CurriculumDTO save(CurriculumDTO curriculumDTO);

    /**
     * Updates a curriculum.
     *
     * @param curriculumDTO the entity to update.
     * @return the persisted entity.
     */
    CurriculumDTO update(CurriculumDTO curriculumDTO);

    /**
     * Partially updates a curriculum.
     *
     * @param curriculumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CurriculumDTO> partialUpdate(CurriculumDTO curriculumDTO);

    /**
     * Get all the curricula.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CurriculumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" curriculum.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CurriculumDTO> findOne(Long id);

    /**
     * Delete the "id" curriculum.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
