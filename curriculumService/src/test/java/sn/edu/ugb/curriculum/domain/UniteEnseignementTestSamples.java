package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UniteEnseignementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UniteEnseignement getUniteEnseignementSample1() {
        Filiere filiere = new Filiere().id(1L).nom("Filiere1").code("FIL1");
        return new UniteEnseignement()
            .id(1L)
            .nom("nom1")
            .code("code1")
            .filiere(filiere);
    }

    public static UniteEnseignement getUniteEnseignementSample2() {
        Filiere filiere = new Filiere().id(2L).nom("Filiere2").code("FIL2");
        return new UniteEnseignement()
            .id(2L)
            .nom("nom2")
            .code("code2")
            .filiere(filiere);
    }

    public static UniteEnseignement getUniteEnseignementRandomSampleGenerator() {
        Filiere filiere = new Filiere()
            .id(longCount.incrementAndGet())
            .nom("Filiere-" + UUID.randomUUID().toString().substring(0, 5))
            .code("FIL" + longCount.incrementAndGet());

        return new UniteEnseignement()
            .id(longCount.incrementAndGet())
            .nom("UE-" + UUID.randomUUID().toString().substring(0, 8))
            .code("UE" + longCount.incrementAndGet())
            .filiere(filiere);
    }
}
