package sn.edu.ugb.grade.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SessionExamenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SessionExamen getSessionExamenSample1() {
        return new SessionExamen().id(1L).nom("nom1");
    }

    public static SessionExamen getSessionExamenSample2() {
        return new SessionExamen().id(2L).nom("nom2");
    }

    public static SessionExamen getSessionExamenRandomSampleGenerator() {
        return new SessionExamen().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
