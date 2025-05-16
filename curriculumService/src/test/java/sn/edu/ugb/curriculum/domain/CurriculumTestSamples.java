package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CurriculumTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Curriculum getCurriculumSample1() {
        Filiere filiere = new Filiere().id(1L);
        UniteEnseignement uniteEnseignement = new UniteEnseignement().id(1L);
        Semestre semestre = new Semestre().id(1L);

        return new Curriculum()
            .id(1L)
            .filiere(filiere)
            .uniteEnseignement(uniteEnseignement)
            .semestre(semestre);
    }

    public static Curriculum getCurriculumSample2() {
        Filiere filiere = new Filiere().id(2L);
        UniteEnseignement uniteEnseignement = new UniteEnseignement().id(2L);
        Semestre semestre = new Semestre().id(2L);

        return new Curriculum()
            .id(2L)
            .filiere(filiere)
            .uniteEnseignement(uniteEnseignement)
            .semestre(semestre);
    }

    public static Curriculum getCurriculumRandomSampleGenerator() {
        Filiere filiere = new Filiere().id(longCount.incrementAndGet());
        UniteEnseignement uniteEnseignement = new UniteEnseignement().id(longCount.incrementAndGet());
        Semestre semestre = new Semestre().id(longCount.incrementAndGet());

        return new Curriculum()
            .id(longCount.incrementAndGet())
            .filiere(filiere)
            .uniteEnseignement(uniteEnseignement)
            .semestre(semestre);
    }
}
