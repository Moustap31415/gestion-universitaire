package sn.edu.ugb.teacher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.teacher.domain.EnseignantTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class EnseignantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enseignant.class);
        Enseignant enseignant1 = getEnseignantSample1();
        Enseignant enseignant2 = new Enseignant();
        assertThat(enseignant1).isNotEqualTo(enseignant2);

        enseignant2.setId(enseignant1.getId());
        assertThat(enseignant1).isEqualTo(enseignant2);

        enseignant2 = getEnseignantSample2();
        assertThat(enseignant1).isNotEqualTo(enseignant2);
    }
}
