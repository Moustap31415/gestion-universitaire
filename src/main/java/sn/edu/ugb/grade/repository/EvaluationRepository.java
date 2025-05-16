package sn.edu.ugb.grade.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.grade.domain.Evaluation;

/**
 * Spring Data JPA repository for the Evaluation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {}
