package sn.edu.ugb.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.student.domain.InscriptionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class InscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscription.class);
        Inscription inscription1 = getInscriptionSample1();
        Inscription inscription2 = new Inscription();
        assertThat(inscription1).isNotEqualTo(inscription2);

        inscription2.setId(inscription1.getId());
        assertThat(inscription1).isEqualTo(inscription2);

        inscription2 = getInscriptionSample2();
        assertThat(inscription1).isNotEqualTo(inscription2);
    }
}
