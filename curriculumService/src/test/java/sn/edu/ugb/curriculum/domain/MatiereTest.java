package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.MatiereTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class MatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matiere.class);
        Matiere matiere1 = getMatiereSample1();
        Matiere matiere2 = new Matiere();
        assertThat(matiere1).isNotEqualTo(matiere2);

        matiere2.setId(matiere1.getId());
        assertThat(matiere1).isEqualTo(matiere2);

        matiere2 = getMatiereSample2();
        assertThat(matiere1).isNotEqualTo(matiere2);
    }
}
