package sn.edu.ugb.grade.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.grade.domain.EvaluationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class EvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
        Evaluation evaluation1 = getEvaluationSample1();
        Evaluation evaluation2 = new Evaluation();
        assertThat(evaluation1).isNotEqualTo(evaluation2);

        evaluation2.setId(evaluation1.getId());
        assertThat(evaluation1).isEqualTo(evaluation2);

        evaluation2 = getEvaluationSample2();
        assertThat(evaluation1).isNotEqualTo(evaluation2);
    }
}
