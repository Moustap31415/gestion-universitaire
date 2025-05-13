package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueAcademiqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoriqueAcademique getHistoriqueAcademiqueSample1() {
        return new HistoriqueAcademique().id(1L).etudiantId(1L).semestreId(1L);
    }

    public static HistoriqueAcademique getHistoriqueAcademiqueSample2() {
        return new HistoriqueAcademique().id(2L).etudiantId(2L).semestreId(2L);
    }

    public static HistoriqueAcademique getHistoriqueAcademiqueRandomSampleGenerator() {
        return new HistoriqueAcademique()
            .id(longCount.incrementAndGet())
            .etudiantId(longCount.incrementAndGet())
            .semestreId(longCount.incrementAndGet());
    }
}
