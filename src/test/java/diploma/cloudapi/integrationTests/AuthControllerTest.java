package diploma.cloudapi.integrationTests;

import diploma.cloudapi.integrationTests.config.AbstractTest;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import lombok.SneakyThrows;
import org.junit.Test;


public class AuthControllerTest extends AbstractTest {

    @Test
    @SneakyThrows
    public void whenAuthUserThenGetNewToken(){
        var createUserBody = new AuthorizationRequest("user", "user");

    }
}
