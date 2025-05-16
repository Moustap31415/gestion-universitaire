package sn.edu.ugb.grade.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.grade.domain.NoteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class NoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = getNoteSample1();
        Note note2 = new Note();
        assertThat(note1).isNotEqualTo(note2);

        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);

        note2 = getNoteSample2();
        assertThat(note1).isNotEqualTo(note2);
    }
}
