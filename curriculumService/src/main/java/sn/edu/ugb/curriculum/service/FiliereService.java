package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.FiliereDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.Filiere}.
 */
public interface FiliereService {
    /**
     * Save a filiere.
     *
     * @param filiereDTO the entity to save.
     * @return the persisted entity.
     */
    FiliereDTO save(FiliereDTO filiereDTO);

    /**
     * Updates a filiere.
     *
     * @param filiereDTO the entity to update.
     * @return the persisted entity.
     */
    FiliereDTO update(FiliereDTO filiereDTO);

    /**
     * Partially updates a filiere.
     *
     * @param filiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiliereDTO> partialUpdate(FiliereDTO filiereDTO);

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiliereDTO> findAll(Pageable pageable);

    /**
     * Get the "id" filiere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiliereDTO> findOne(Long id);

    /**
     * Delete the "id" filiere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
