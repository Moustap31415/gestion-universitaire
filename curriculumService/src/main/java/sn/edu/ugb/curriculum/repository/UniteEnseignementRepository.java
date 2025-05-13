package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.UniteEnseignement;

/**
 * Spring Data JPA repository for the UniteEnseignement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniteEnseignementRepository extends JpaRepository<UniteEnseignement, Long> {}
