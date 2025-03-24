package diploma.cloudapi.security;

import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.ActiveTokenRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final ActiveTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        //todo все еще не тот пользак отображается
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        var jwtToken = authorization.substring(7);


        if (!jwtTokenService.isTokenValid(jwtToken)){
            log.info("Jwt token not valid");
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenRepository.findByToken(jwtToken).isEmpty()) {
            log.info("Jwt token was not found in active tokens");
            filterChain.doFilter(request, response);
            return;
        }

        var login = jwtTokenService.getLoginFromToken(jwtToken);
        var role = jwtTokenService.getRoleFromToken(jwtToken);
        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,null, List.of(new SimpleGrantedAuthority("ROLE"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
