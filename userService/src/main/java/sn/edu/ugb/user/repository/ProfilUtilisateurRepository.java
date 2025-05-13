package sn.edu.ugb.user.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.user.domain.ProfilUtilisateur;

/**
 * Spring Data JPA repository for the ProfilUtilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilUtilisateurRepository extends JpaRepository<ProfilUtilisateur, Long> {}
