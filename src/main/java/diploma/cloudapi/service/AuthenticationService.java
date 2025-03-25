package diploma.cloudapi.service;

import diploma.cloudapi.entity.ActiveTokenEntity;
import diploma.cloudapi.repository.ActiveTokenRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.web.dto.AuthTokenResponse;
import diploma.cloudapi.web.dto.AuthorizationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final ActiveTokenRepository activeTokenRepository;

    public String authenticateUser(AuthorizationRequest request){
        log.info("Request to authenticate user {}", request.getLogin());
        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException("Username or password is incorrect"));
        if(!passwordEncoder.matches(request.getPassword(), user.getHashedPass())){
            throw new BadCredentialsException("Username or password is incorrect");
        }
        String token = jwtTokenService.generateToken(user);
        activeTokenRepository.save(new ActiveTokenEntity(token, user.getLogin(), jwtTokenService.getExpiration(token)));
        return token;
    }

    public void logout(String token){
        activeTokenRepository.deleteActiveTokenEntityByToken(token);
    }
}