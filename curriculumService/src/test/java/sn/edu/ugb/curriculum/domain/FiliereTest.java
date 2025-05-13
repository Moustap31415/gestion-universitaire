package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.FiliereTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class FiliereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filiere.class);
        Filiere filiere1 = getFiliereSample1();
        Filiere filiere2 = new Filiere();
        assertThat(filiere1).isNotEqualTo(filiere2);

        filiere2.setId(filiere1.getId());
        assertThat(filiere1).isEqualTo(filiere2);

        filiere2 = getFiliereSample2();
        assertThat(filiere1).isNotEqualTo(filiere2);
    }
}
