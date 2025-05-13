package sn.edu.ugb.user.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.user.service.dto.ProfilUtilisateurDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.user.domain.ProfilUtilisateur}.
 */
public interface ProfilUtilisateurService {
    /**
     * Save a profilUtilisateur.
     *
     * @param profilUtilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    ProfilUtilisateurDTO save(ProfilUtilisateurDTO profilUtilisateurDTO);

    /**
     * Updates a profilUtilisateur.
     *
     * @param profilUtilisateurDTO the entity to update.
     * @return the persisted entity.
     */
    ProfilUtilisateurDTO update(ProfilUtilisateurDTO profilUtilisateurDTO);

    /**
     * Partially updates a profilUtilisateur.
     *
     * @param profilUtilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfilUtilisateurDTO> partialUpdate(ProfilUtilisateurDTO profilUtilisateurDTO);

    /**
     * Get all the profilUtilisateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfilUtilisateurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" profilUtilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfilUtilisateurDTO> findOne(Long id);

    /**
     * Delete the "id" profilUtilisateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
