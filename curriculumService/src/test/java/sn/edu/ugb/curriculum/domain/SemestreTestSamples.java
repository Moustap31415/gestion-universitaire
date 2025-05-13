package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SemestreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Semestre getSemestreSample1() {
        return new Semestre().id(1L);
    }

    public static Semestre getSemestreSample2() {
        return new Semestre().id(2L);
    }

    public static Semestre getSemestreRandomSampleGenerator() {
        return new Semestre().id(longCount.incrementAndGet());
    }
}
