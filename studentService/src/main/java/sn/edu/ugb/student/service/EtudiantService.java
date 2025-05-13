package sn.edu.ugb.student.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.student.service.dto.EtudiantDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.student.domain.Etudiant}.
 */
public interface EtudiantService {
    /**
     * Save a etudiant.
     *
     * @param etudiantDTO the entity to save.
     * @return the persisted entity.
     */
    EtudiantDTO save(EtudiantDTO etudiantDTO);

    /**
     * Updates a etudiant.
     *
     * @param etudiantDTO the entity to update.
     * @return the persisted entity.
     */
    EtudiantDTO update(EtudiantDTO etudiantDTO);

    /**
     * Partially updates a etudiant.
     *
     * @param etudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO);

    /**
     * Get all the etudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EtudiantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" etudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" etudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
