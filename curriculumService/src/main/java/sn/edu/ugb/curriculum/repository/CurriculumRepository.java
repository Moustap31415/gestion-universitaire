package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.Curriculum;

/**
 * Spring Data JPA repository for the Curriculum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {}
