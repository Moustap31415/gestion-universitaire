package sn.edu.ugb.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.user.domain.ProfilUtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.user.web.rest.TestUtil;

class ProfilUtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilUtilisateur.class);
        ProfilUtilisateur profilUtilisateur1 = getProfilUtilisateurSample1();
        ProfilUtilisateur profilUtilisateur2 = new ProfilUtilisateur();
        assertThat(profilUtilisateur1).isNotEqualTo(profilUtilisateur2);

        profilUtilisateur2.setId(profilUtilisateur1.getId());
        assertThat(profilUtilisateur1).isEqualTo(profilUtilisateur2);

        profilUtilisateur2 = getProfilUtilisateurSample2();
        assertThat(profilUtilisateur1).isNotEqualTo(profilUtilisateur2);
    }
}
