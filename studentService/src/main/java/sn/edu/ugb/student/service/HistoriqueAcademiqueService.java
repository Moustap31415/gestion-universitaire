package sn.edu.ugb.student.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.student.service.dto.HistoriqueAcademiqueDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.student.domain.HistoriqueAcademique}.
 */
public interface HistoriqueAcademiqueService {
    /**
     * Save a historiqueAcademique.
     *
     * @param historiqueAcademiqueDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriqueAcademiqueDTO save(HistoriqueAcademiqueDTO historiqueAcademiqueDTO);

    /**
     * Updates a historiqueAcademique.
     *
     * @param historiqueAcademiqueDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriqueAcademiqueDTO update(HistoriqueAcademiqueDTO historiqueAcademiqueDTO);

    /**
     * Partially updates a historiqueAcademique.
     *
     * @param historiqueAcademiqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriqueAcademiqueDTO> partialUpdate(HistoriqueAcademiqueDTO historiqueAcademiqueDTO);

    /**
     * Get all the historiqueAcademiques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueAcademiqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historiqueAcademique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueAcademiqueDTO> findOne(Long id);

    /**
     * Delete the "id" historiqueAcademique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
