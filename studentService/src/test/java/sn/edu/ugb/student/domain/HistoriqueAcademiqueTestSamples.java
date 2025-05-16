package sn.edu.ugb.student.domain;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import sn.edu.ugb.student.domain.enumeration.StatutAcademique;

public class HistoriqueAcademiqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoriqueAcademique getHistoriqueAcademiqueSample1() {
        Etudiant etudiant = new Etudiant().id(1L);
        return new HistoriqueAcademique()
            .id(1L)
            .statut(StatutAcademique.EN_COURS)
            .dateInscription(Instant.ofEpochMilli(1000L))
            .etudiant(etudiant)
            .semestreId(1L);
    }

    public static HistoriqueAcademique getHistoriqueAcademiqueSample2() {
        Etudiant etudiant = new Etudiant().id(2L);
        return new HistoriqueAcademique()
            .id(2L)
            .statut(StatutAcademique.VALIDE)
            .dateInscription(Instant.ofEpochMilli(2000L))
            .etudiant(etudiant)
            .semestreId(2L);
    }

    public static HistoriqueAcademique getHistoriqueAcademiqueRandomSampleGenerator() {
        Etudiant etudiant = new Etudiant().id(longCount.incrementAndGet());
        return new HistoriqueAcademique()
            .id(longCount.incrementAndGet())
            .statut(StatutAcademique.values()[random.nextInt(StatutAcademique.values().length)])
            .dateInscription(Instant.now().minusSeconds(random.nextInt(3600)))
            .etudiant(etudiant)
            .semestreId(longCount.incrementAndGet());
    }
}
