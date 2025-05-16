package sn.edu.ugb.grade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.edu.ugb.grade.config.AsyncSyncConfiguration;
import sn.edu.ugb.grade.config.EmbeddedSQL;
import sn.edu.ugb.grade.config.JacksonConfiguration;
import sn.edu.ugb.grade.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { NoteServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
public @interface IntegrationTest {
}
