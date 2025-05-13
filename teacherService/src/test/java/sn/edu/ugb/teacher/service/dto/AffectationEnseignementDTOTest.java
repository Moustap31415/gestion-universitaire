package sn.edu.ugb.teacher.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class AffectationEnseignementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationEnseignementDTO.class);
        AffectationEnseignementDTO affectationEnseignementDTO1 = new AffectationEnseignementDTO();
        affectationEnseignementDTO1.setId(1L);
        AffectationEnseignementDTO affectationEnseignementDTO2 = new AffectationEnseignementDTO();
        assertThat(affectationEnseignementDTO1).isNotEqualTo(affectationEnseignementDTO2);
        affectationEnseignementDTO2.setId(affectationEnseignementDTO1.getId());
        assertThat(affectationEnseignementDTO1).isEqualTo(affectationEnseignementDTO2);
        affectationEnseignementDTO2.setId(2L);
        assertThat(affectationEnseignementDTO1).isNotEqualTo(affectationEnseignementDTO2);
        affectationEnseignementDTO1.setId(null);
        assertThat(affectationEnseignementDTO1).isNotEqualTo(affectationEnseignementDTO2);
    }
}
