package diploma.cloudapi.serviceTests;

import diploma.cloudapi.entity.ActiveTokenEntity;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.ActiveTokenRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.service.AuthenticationService;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private ActiveTokenRepository activeTokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testUser = new UserEntity();
        testUser.setLogin("testUserLogin");
        testUser.setHashedPass("testUserPassword");
    }

    @Test
    public void successAuthenticationTest() {
        AuthorizationRequest request = new AuthorizationRequest("testUserLogin", "testUserPassword");
        when(userRepository.findByLogin("testUserLogin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("testUserPassword", "testUserPassword")).thenReturn(true);
        when(jwtTokenService.generateToken(testUser)).thenReturn("mockToken");
        when(jwtTokenService.getExpiration(anyString())).thenReturn(Instant.MAX);

        String token = authenticationService.authenticateUser(request);

        assertNotNull(token);
        assertEquals("mockToken", token);
        verify(activeTokenRepository, times(1)).save(any(ActiveTokenEntity.class));
    }

    @Test
    public void authenticateUserWithWrongPasswordTest() {
        AuthorizationRequest request = new AuthorizationRequest("testUserLogin", "wrongPassword");
        when(userRepository.findByLogin("testUserLogin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "testUserPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUser(request));
        verify(activeTokenRepository, never()).save(any());
    }

    @Test
    void userNotFoundAfterAuthenticationTest() {
        AuthorizationRequest request = new AuthorizationRequest("unknownUser", "password");
        when(userRepository.findByLogin("unknownUser")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUser(request));
        verify(activeTokenRepository, never()).save(any());
    }

    @Test
    void logoutTest() {
        authenticationService.logout("mockToken");
        verify(activeTokenRepository, times(1)).deleteActiveTokenEntityByToken("mockToken");
    }
}
