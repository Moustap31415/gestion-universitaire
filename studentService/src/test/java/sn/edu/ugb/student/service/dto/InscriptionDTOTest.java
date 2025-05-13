package sn.edu.ugb.student.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class InscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionDTO.class);
        InscriptionDTO inscriptionDTO1 = new InscriptionDTO();
        inscriptionDTO1.setId(1L);
        InscriptionDTO inscriptionDTO2 = new InscriptionDTO();
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
        inscriptionDTO2.setId(inscriptionDTO1.getId());
        assertThat(inscriptionDTO1).isEqualTo(inscriptionDTO2);
        inscriptionDTO2.setId(2L);
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
        inscriptionDTO1.setId(null);
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
    }
}
