package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.UniteEnseignementDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.UniteEnseignement}.
 */
public interface UniteEnseignementService {
    /**
     * Save a uniteEnseignement.
     *
     * @param uniteEnseignementDTO the entity to save.
     * @return the persisted entity.
     */
    UniteEnseignementDTO save(UniteEnseignementDTO uniteEnseignementDTO);

    /**
     * Updates a uniteEnseignement.
     *
     * @param uniteEnseignementDTO the entity to update.
     * @return the persisted entity.
     */
    UniteEnseignementDTO update(UniteEnseignementDTO uniteEnseignementDTO);

    /**
     * Partially updates a uniteEnseignement.
     *
     * @param uniteEnseignementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniteEnseignementDTO> partialUpdate(UniteEnseignementDTO uniteEnseignementDTO);

    /**
     * Get all the uniteEnseignements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniteEnseignementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" uniteEnseignement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniteEnseignementDTO> findOne(Long id);

    /**
     * Delete the "id" uniteEnseignement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
