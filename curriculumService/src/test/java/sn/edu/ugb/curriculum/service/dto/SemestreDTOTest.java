package sn.edu.ugb.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class SemestreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemestreDTO.class);
        SemestreDTO semestreDTO1 = new SemestreDTO();
        semestreDTO1.setId(1L);
        SemestreDTO semestreDTO2 = new SemestreDTO();
        assertThat(semestreDTO1).isNotEqualTo(semestreDTO2);
        semestreDTO2.setId(semestreDTO1.getId());
        assertThat(semestreDTO1).isEqualTo(semestreDTO2);
        semestreDTO2.setId(2L);
        assertThat(semestreDTO1).isNotEqualTo(semestreDTO2);
        semestreDTO1.setId(null);
        assertThat(semestreDTO1).isNotEqualTo(semestreDTO2);
    }
}
