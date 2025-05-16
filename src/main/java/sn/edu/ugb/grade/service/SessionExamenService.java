package sn.edu.ugb.grade.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.grade.service.dto.SessionExamenDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.SessionExamen}.
 */
public interface SessionExamenService {
    /**
     * Save a sessionExamen.
     *
     * @param sessionExamenDTO the entity to save.
     * @return the persisted entity.
     */
    SessionExamenDTO save(SessionExamenDTO sessionExamenDTO);

    /**
     * Updates a sessionExamen.
     *
     * @param sessionExamenDTO the entity to update.
     * @return the persisted entity.
     */
    SessionExamenDTO update(SessionExamenDTO sessionExamenDTO);

    /**
     * Partially updates a sessionExamen.
     *
     * @param sessionExamenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionExamenDTO> partialUpdate(SessionExamenDTO sessionExamenDTO);

    /**
     * Get all the sessionExamen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SessionExamenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sessionExamen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionExamenDTO> findOne(Long id);

    /**
     * Delete the "id" sessionExamen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
