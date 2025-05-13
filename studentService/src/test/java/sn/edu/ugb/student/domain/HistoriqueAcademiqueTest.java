package sn.edu.ugb.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.student.domain.HistoriqueAcademiqueTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class HistoriqueAcademiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueAcademique.class);
        HistoriqueAcademique historiqueAcademique1 = getHistoriqueAcademiqueSample1();
        HistoriqueAcademique historiqueAcademique2 = new HistoriqueAcademique();
        assertThat(historiqueAcademique1).isNotEqualTo(historiqueAcademique2);

        historiqueAcademique2.setId(historiqueAcademique1.getId());
        assertThat(historiqueAcademique1).isEqualTo(historiqueAcademique2);

        historiqueAcademique2 = getHistoriqueAcademiqueSample2();
        assertThat(historiqueAcademique1).isNotEqualTo(historiqueAcademique2);
    }
}
