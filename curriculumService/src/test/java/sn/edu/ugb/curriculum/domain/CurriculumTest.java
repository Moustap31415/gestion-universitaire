package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.CurriculumTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class CurriculumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Curriculum.class);
        Curriculum curriculum1 = getCurriculumSample1();
        Curriculum curriculum2 = new Curriculum();
        assertThat(curriculum1).isNotEqualTo(curriculum2);

        curriculum2.setId(curriculum1.getId());
        assertThat(curriculum1).isEqualTo(curriculum2);

        curriculum2 = getCurriculumSample2();
        assertThat(curriculum1).isNotEqualTo(curriculum2);
    }
}
