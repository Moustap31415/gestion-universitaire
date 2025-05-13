package sn.edu.ugb.user.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.user.service.dto.RoleDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.user.domain.Role}.
 */
public interface RoleService {
    /**
     * Save a role.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    RoleDTO save(RoleDTO roleDTO);

    /**
     * Updates a role.
     *
     * @param roleDTO the entity to update.
     * @return the persisted entity.
     */
    RoleDTO update(RoleDTO roleDTO);

    /**
     * Partially updates a role.
     *
     * @param roleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoleDTO> partialUpdate(RoleDTO roleDTO);

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" role.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleDTO> findOne(Long id);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
