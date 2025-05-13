package sn.edu.ugb.curriculum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.edu.ugb.curriculum.config.AsyncSyncConfiguration;
import sn.edu.ugb.curriculum.config.EmbeddedSQL;
import sn.edu.ugb.curriculum.config.JacksonConfiguration;
import sn.edu.ugb.curriculum.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { CurriculumServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
public @interface IntegrationTest {
}
