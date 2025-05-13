package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.UniteEnseignementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class UniteEnseignementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniteEnseignement.class);
        UniteEnseignement uniteEnseignement1 = getUniteEnseignementSample1();
        UniteEnseignement uniteEnseignement2 = new UniteEnseignement();
        assertThat(uniteEnseignement1).isNotEqualTo(uniteEnseignement2);

        uniteEnseignement2.setId(uniteEnseignement1.getId());
        assertThat(uniteEnseignement1).isEqualTo(uniteEnseignement2);

        uniteEnseignement2 = getUniteEnseignementSample2();
        assertThat(uniteEnseignement1).isNotEqualTo(uniteEnseignement2);
    }
}
