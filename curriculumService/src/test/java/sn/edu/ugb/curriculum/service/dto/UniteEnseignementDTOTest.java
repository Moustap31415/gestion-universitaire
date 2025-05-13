package sn.edu.ugb.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class UniteEnseignementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniteEnseignementDTO.class);
        UniteEnseignementDTO uniteEnseignementDTO1 = new UniteEnseignementDTO();
        uniteEnseignementDTO1.setId(1L);
        UniteEnseignementDTO uniteEnseignementDTO2 = new UniteEnseignementDTO();
        assertThat(uniteEnseignementDTO1).isNotEqualTo(uniteEnseignementDTO2);
        uniteEnseignementDTO2.setId(uniteEnseignementDTO1.getId());
        assertThat(uniteEnseignementDTO1).isEqualTo(uniteEnseignementDTO2);
        uniteEnseignementDTO2.setId(2L);
        assertThat(uniteEnseignementDTO1).isNotEqualTo(uniteEnseignementDTO2);
        uniteEnseignementDTO1.setId(null);
        assertThat(uniteEnseignementDTO1).isNotEqualTo(uniteEnseignementDTO2);
    }
}
