package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inscription getInscriptionSample1() {
        return new Inscription()
            .id(1L)
            .enCours(true)
            .etudiant(new Etudiant().id(1L))
            .filiereId(1L)
            .semestreId(1L);
    }

    public static Inscription getInscriptionSample2() {
        return new Inscription()
            .id(2L)
            .enCours(false)
            .etudiant(new Etudiant().id(2L))
            .filiereId(2L)
            .semestreId(2L);
    }

    public static Inscription getInscriptionRandomSampleGenerator() {
        return new Inscription()
            .id(longCount.incrementAndGet())
            .enCours(random.nextBoolean())
            .etudiant(new Etudiant().id(longCount.incrementAndGet()))
            .filiereId(longCount.incrementAndGet())
            .semestreId(longCount.incrementAndGet());
    }
}
