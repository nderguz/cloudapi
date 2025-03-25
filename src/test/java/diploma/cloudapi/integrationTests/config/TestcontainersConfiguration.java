package diploma.cloudapi.integrationTests.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    PostgreSQLContainer<?> postgreSQLContainer(){
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
                .withReuse(true)
                .withUsername("admin")
                .withPassword("admin")
                .withDatabaseName("public");
    }
}
