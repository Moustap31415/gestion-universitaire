package sn.edu.ugb.student.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.student.domain.HistoriqueAcademique;

/**
 * Spring Data JPA repository for the HistoriqueAcademique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueAcademiqueRepository extends JpaRepository<HistoriqueAcademique, Long> {}
