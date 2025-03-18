package diploma.cloudapi.utils;

import diploma.cloudapi.entity.RoleType;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserInitializator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        generateAdminUser();
        generateCommonUser();
    }

    private void generateAdminUser() {
        boolean isUserNotExist = userRepository.findByLogin("admin").isEmpty();
        if(isUserNotExist){
            var user = generateUser("admin", "admin", RoleType.ROLE_ADMIN);
            userRepository.save(user);
        }
    }

    private void generateCommonUser() {
        boolean isUserNotExist = userRepository.findByLogin("user").isEmpty();
        if(isUserNotExist){
            var user = generateUser("user", "user", RoleType.ROLE_USER);
            userRepository.save(user);
        }
    }

    private UserEntity generateUser(String login, String password, RoleType role) {
        return UserEntity.builder()
                .login(login)
                .hashedPass(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .build();
    }
}
