package diploma.cloudapi.integration;

import diploma.cloudapi.repository.ActiveTokenRepository;
import diploma.cloudapi.repository.FileRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.service.AuthenticationService;
import diploma.cloudapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AbstractTest extends TestcontainersConfig{

    @Autowired
    protected AuthenticationService authenticationService;

    @Autowired
    protected ActiveTokenRepository activeTokenRepository;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected FileRepository fileRepository;

    @Autowired
    protected JwtTokenService jwtTokenService;

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
