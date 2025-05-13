package sn.edu.ugb.grade.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Note getNoteSample1() {
        return new Note().id(1L).commentaires("commentaires1").etudiantId(1L).evaluationId(1L);
    }

    public static Note getNoteSample2() {
        return new Note().id(2L).commentaires("commentaires2").etudiantId(2L).evaluationId(2L);
    }

    public static Note getNoteRandomSampleGenerator() {
        return new Note()
            .id(longCount.incrementAndGet())
            .commentaires(UUID.randomUUID().toString())
            .etudiantId(longCount.incrementAndGet())
            .evaluationId(longCount.incrementAndGet());
    }
}
