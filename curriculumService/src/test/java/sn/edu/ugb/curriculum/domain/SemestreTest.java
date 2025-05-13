package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.SemestreTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class SemestreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semestre.class);
        Semestre semestre1 = getSemestreSample1();
        Semestre semestre2 = new Semestre();
        assertThat(semestre1).isNotEqualTo(semestre2);

        semestre2.setId(semestre1.getId());
        assertThat(semestre1).isEqualTo(semestre2);

        semestre2 = getSemestreSample2();
        assertThat(semestre1).isNotEqualTo(semestre2);
    }
}
