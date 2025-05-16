package sn.edu.ugb.grade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class SessionExamenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionExamenDTO.class);
        SessionExamenDTO sessionExamenDTO1 = new SessionExamenDTO();
        sessionExamenDTO1.setId(1L);
        SessionExamenDTO sessionExamenDTO2 = new SessionExamenDTO();
        assertThat(sessionExamenDTO1).isNotEqualTo(sessionExamenDTO2);
        sessionExamenDTO2.setId(sessionExamenDTO1.getId());
        assertThat(sessionExamenDTO1).isEqualTo(sessionExamenDTO2);
        sessionExamenDTO2.setId(2L);
        assertThat(sessionExamenDTO1).isNotEqualTo(sessionExamenDTO2);
        sessionExamenDTO1.setId(null);
        assertThat(sessionExamenDTO1).isNotEqualTo(sessionExamenDTO2);
    }
}
