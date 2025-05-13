package sn.edu.ugb.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilUtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfilUtilisateur getProfilUtilisateurSample1() {
        return new ProfilUtilisateur().id(1L).nom("nom1").prenom("prenom1").email("email1").telephone("telephone1").roleId(1L);
    }

    public static ProfilUtilisateur getProfilUtilisateurSample2() {
        return new ProfilUtilisateur().id(2L).nom("nom2").prenom("prenom2").email("email2").telephone("telephone2").roleId(2L);
    }

    public static ProfilUtilisateur getProfilUtilisateurRandomSampleGenerator() {
        return new ProfilUtilisateur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .roleId(longCount.incrementAndGet());
    }
}
