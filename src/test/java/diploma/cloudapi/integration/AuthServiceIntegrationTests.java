package diploma.cloudapi.integration;

import diploma.cloudapi.entity.ActiveTokenEntity;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceIntegrationTests extends AbstractTest{

    @Test
    public void authenticateUserSuccessTest(){
        var request = new AuthorizationRequest("test_user1", "test_user1");
        String token = authenticationService.authenticateUser(request);
        assertNotNull(token);
        assertFalse(activeTokenRepository.findAll().isEmpty());
    }

    @Test
    public void authenticateInvalidUserTest(){
        var request = new AuthorizationRequest("random", "user");
        assertThrows(Exception.class, () -> authenticationService.authenticateUser(request));
    }

    @Test
    public void correctLogoutTest(){
        String token = "$2a$12$RzipMYO0aCP5CafQ1PdPP.m3XejJ92mPTPiDq6PNS0GkuDT7q71PS";
        activeTokenRepository.save(new ActiveTokenEntity(token, "test_user2", Instant.now()));
        authenticationService.logout(token);
        assertTrue(activeTokenRepository.findAll().isEmpty());
    }
}