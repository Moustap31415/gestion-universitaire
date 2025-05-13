package sn.edu.ugb.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.user.domain.RoleTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.user.web.rest.TestUtil;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }
}
