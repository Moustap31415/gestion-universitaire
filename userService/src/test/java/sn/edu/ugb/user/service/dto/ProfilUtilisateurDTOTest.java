package sn.edu.ugb.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.user.web.rest.TestUtil;

class ProfilUtilisateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilUtilisateurDTO.class);
        ProfilUtilisateurDTO profilUtilisateurDTO1 = new ProfilUtilisateurDTO();
        profilUtilisateurDTO1.setId(1L);
        ProfilUtilisateurDTO profilUtilisateurDTO2 = new ProfilUtilisateurDTO();
        assertThat(profilUtilisateurDTO1).isNotEqualTo(profilUtilisateurDTO2);
        profilUtilisateurDTO2.setId(profilUtilisateurDTO1.getId());
        assertThat(profilUtilisateurDTO1).isEqualTo(profilUtilisateurDTO2);
        profilUtilisateurDTO2.setId(2L);
        assertThat(profilUtilisateurDTO1).isNotEqualTo(profilUtilisateurDTO2);
        profilUtilisateurDTO1.setId(null);
        assertThat(profilUtilisateurDTO1).isNotEqualTo(profilUtilisateurDTO2);
    }
}
