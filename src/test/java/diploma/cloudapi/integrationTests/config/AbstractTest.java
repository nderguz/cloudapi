package diploma.cloudapi.integrationTests.config;


import diploma.cloudapi.entity.RoleType;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.ActiveTokenRepository;
import diploma.cloudapi.repository.FileRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.service.AuthenticationService;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

@SpringBootTest
@Transactional
@Sql("classpath:db/init_test_data.sql")
@Import({TestcontainersConfiguration.class, TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class AbstractTest {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected FileRepository fileRepository;

    @Autowired
    protected ActiveTokenRepository activeTokenRepository;

    @Autowired
    protected JwtTokenService jwtTokenService;

    @Autowired
    protected AuthenticationService authenticationService;

    @BeforeEach
    public  void generateTestUsers(){
        generateUserToDb("testUser1", "testUser1", RoleType.ROLE_USER);
        generateUserToDb("testUser2", "testUser2", RoleType.ROLE_ADMIN);
    }

    @AfterEach
    public void removeTestUsers(){
        userRepository.deleteAll();
        activeTokenRepository.deleteAll();
        fileRepository.deleteAll();
    }

    private void generateUserToDb(String login, String password, RoleType role){
        boolean isUserNotExist = userRepository.findByLogin(login).isEmpty();
        if(isUserNotExist){
            var user = UserEntity.builder()
                    .login(login)
                    .hashedPass(passwordEncoder.encode(password))
                    .roles(Set.of(role))
                    .build();
            userRepository.save(user);
        }
    }

    @SneakyThrows
    private String getLoginRequestBody(){
        return objectMapper.writeValueAsString(new AuthorizationRequest("testUser", "testUser"));
    }

    @SneakyThrows
    private String getLoginResponseBody(UserEntity userEntity){
        return objectMapper.writeValueAsString(jwtTokenService.generateToken(userEntity));
    }
}
