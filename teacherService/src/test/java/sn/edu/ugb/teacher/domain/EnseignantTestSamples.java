package sn.edu.ugb.teacher.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnseignantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enseignant getEnseignantSample1() {
        return new Enseignant().id(1L).numeroEnseignant("numeroEnseignant1").specialisation("specialisation1").utilisateurId(1L);
    }

    public static Enseignant getEnseignantSample2() {
        return new Enseignant().id(2L).numeroEnseignant("numeroEnseignant2").specialisation("specialisation2").utilisateurId(2L);
    }

    public static Enseignant getEnseignantRandomSampleGenerator() {
        return new Enseignant()
            .id(longCount.incrementAndGet())
            .numeroEnseignant(UUID.randomUUID().toString())
            .specialisation(UUID.randomUUID().toString())
            .utilisateurId(longCount.incrementAndGet());
    }
}
