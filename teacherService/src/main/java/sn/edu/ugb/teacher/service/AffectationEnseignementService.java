package sn.edu.ugb.teacher.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.teacher.service.dto.AffectationEnseignementDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.teacher.domain.AffectationEnseignement}.
 */
public interface AffectationEnseignementService {
    /**
     * Save a affectationEnseignement.
     *
     * @param affectationEnseignementDTO the entity to save.
     * @return the persisted entity.
     */
    AffectationEnseignementDTO save(AffectationEnseignementDTO affectationEnseignementDTO);

    /**
     * Updates a affectationEnseignement.
     *
     * @param affectationEnseignementDTO the entity to update.
     * @return the persisted entity.
     */
    AffectationEnseignementDTO update(AffectationEnseignementDTO affectationEnseignementDTO);

    /**
     * Partially updates a affectationEnseignement.
     *
     * @param affectationEnseignementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AffectationEnseignementDTO> partialUpdate(AffectationEnseignementDTO affectationEnseignementDTO);

    /**
     * Get all the affectationEnseignements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AffectationEnseignementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" affectationEnseignement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AffectationEnseignementDTO> findOne(Long id);

    /**
     * Delete the "id" affectationEnseignement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
