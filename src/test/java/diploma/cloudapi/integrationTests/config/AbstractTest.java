package diploma.cloudapi.integrationTests.config;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class, TestConfiguration.class})
public abstract class AbstractTest {

}
