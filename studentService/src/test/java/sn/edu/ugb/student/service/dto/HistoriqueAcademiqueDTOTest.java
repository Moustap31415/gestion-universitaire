package sn.edu.ugb.student.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class HistoriqueAcademiqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueAcademiqueDTO.class);
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO1 = new HistoriqueAcademiqueDTO();
        historiqueAcademiqueDTO1.setId(1L);
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO2 = new HistoriqueAcademiqueDTO();
        assertThat(historiqueAcademiqueDTO1).isNotEqualTo(historiqueAcademiqueDTO2);
        historiqueAcademiqueDTO2.setId(historiqueAcademiqueDTO1.getId());
        assertThat(historiqueAcademiqueDTO1).isEqualTo(historiqueAcademiqueDTO2);
        historiqueAcademiqueDTO2.setId(2L);
        assertThat(historiqueAcademiqueDTO1).isNotEqualTo(historiqueAcademiqueDTO2);
        historiqueAcademiqueDTO1.setId(null);
        assertThat(historiqueAcademiqueDTO1).isNotEqualTo(historiqueAcademiqueDTO2);
    }
}
