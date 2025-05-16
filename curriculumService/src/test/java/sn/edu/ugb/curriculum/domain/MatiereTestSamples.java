package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MatiereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Matiere getMatiereSample1() {
        UniteEnseignement uniteEnseignement = new UniteEnseignement().id(1L).nom("UE1").code("UE001");
        return new Matiere()
            .id(1L)
            .nom("nom1")
            .heures(1)
            .credits(1)
            .uniteEnseignement(uniteEnseignement);
    }

    public static Matiere getMatiereSample2() {
        UniteEnseignement uniteEnseignement = new UniteEnseignement().id(2L).nom("UE2").code("UE002");
        return new Matiere()
            .id(2L)
            .nom("nom2")
            .heures(2)
            .credits(2)
            .uniteEnseignement(uniteEnseignement);
    }

    public static Matiere getMatiereRandomSampleGenerator() {
        UniteEnseignement uniteEnseignement = new UniteEnseignement()
            .id(longCount.incrementAndGet())
            .nom("UE-" + UUID.randomUUID().toString().substring(0, 8))
            .code("UE" + longCount.incrementAndGet());

        return new Matiere()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .heures(intCount.incrementAndGet())
            .credits(intCount.incrementAndGet())
            .uniteEnseignement(uniteEnseignement);
    }
}
