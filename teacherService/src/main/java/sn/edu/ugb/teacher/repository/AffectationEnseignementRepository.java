package sn.edu.ugb.teacher.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.teacher.domain.AffectationEnseignement;

/**
 * Spring Data JPA repository for the AffectationEnseignement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationEnseignementRepository extends JpaRepository<AffectationEnseignement, Long> {}
