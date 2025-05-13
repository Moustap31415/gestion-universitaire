package sn.edu.ugb.grade.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.grade.domain.SessionExamen;

/**
 * Spring Data JPA repository for the SessionExamen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionExamenRepository extends JpaRepository<SessionExamen, Long> {}
