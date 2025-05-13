package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Etudiant getEtudiantSample1() {
        return new Etudiant()
            .id(1L)
            .numeroEtudiant("numeroEtudiant1")
            .adresse("adresse1")
            .formationActuelle("formationActuelle1")
            .utilisateurId(1L);
    }

    public static Etudiant getEtudiantSample2() {
        return new Etudiant()
            .id(2L)
            .numeroEtudiant("numeroEtudiant2")
            .adresse("adresse2")
            .formationActuelle("formationActuelle2")
            .utilisateurId(2L);
    }

    public static Etudiant getEtudiantRandomSampleGenerator() {
        return new Etudiant()
            .id(longCount.incrementAndGet())
            .numeroEtudiant(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .formationActuelle(UUID.randomUUID().toString())
            .utilisateurId(longCount.incrementAndGet());
    }
}
