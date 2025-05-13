package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.Semestre;

/**
 * Spring Data JPA repository for the Semestre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long> {}
