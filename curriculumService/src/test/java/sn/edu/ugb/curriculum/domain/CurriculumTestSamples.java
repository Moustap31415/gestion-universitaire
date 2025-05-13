package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CurriculumTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Curriculum getCurriculumSample1() {
        return new Curriculum().id(1L).filiereId(1L).moduleId(1L).semestreId(1L);
    }

    public static Curriculum getCurriculumSample2() {
        return new Curriculum().id(2L).filiereId(2L).moduleId(2L).semestreId(2L);
    }

    public static Curriculum getCurriculumRandomSampleGenerator() {
        return new Curriculum()
            .id(longCount.incrementAndGet())
            .filiereId(longCount.incrementAndGet())
            .moduleId(longCount.incrementAndGet())
            .semestreId(longCount.incrementAndGet());
    }
}
