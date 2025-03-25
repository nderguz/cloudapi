package diploma.cloudapi.integrationTests;

import diploma.cloudapi.integrationTests.config.AbstractTest;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AuthIntegrationTest extends AbstractTest {

    @Test
    void whenAuthUserThenGetNewToken(){
        var authRequest = new AuthorizationRequest("user", "user");
        String token = authenticationService.authenticateUser(authRequest);
        assertNotNull(token);
        assertFalse(activeTokenRepository.findAll().isEmpty());
    }

}
