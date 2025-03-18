package diploma.cloudapi.security;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
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
        var login = jwtTokenService.getLoginFromToken(jwtToken);
        var role = jwtTokenService.getRoleFromToken(jwtToken);
        var user = userRepository.findByLogin(login);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,null, List.of(new SimpleGrantedAuthority("ROLE"))
        );

        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
