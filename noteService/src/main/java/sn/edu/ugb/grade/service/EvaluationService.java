package sn.edu.ugb.grade.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.grade.service.dto.EvaluationDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.Evaluation}.
 */
public interface EvaluationService {
    /**
     * Save a evaluation.
     *
     * @param evaluationDTO the entity to save.
     * @return the persisted entity.
     */
    EvaluationDTO save(EvaluationDTO evaluationDTO);

    /**
     * Updates a evaluation.
     *
     * @param evaluationDTO the entity to update.
     * @return the persisted entity.
     */
    EvaluationDTO update(EvaluationDTO evaluationDTO);

    /**
     * Partially updates a evaluation.
     *
     * @param evaluationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvaluationDTO> partialUpdate(EvaluationDTO evaluationDTO);

    /**
     * Get all the evaluations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvaluationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" evaluation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvaluationDTO> findOne(Long id);

    /**
     * Delete the "id" evaluation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
