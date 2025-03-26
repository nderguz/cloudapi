package diploma.cloudapi.security;

import diploma.cloudapi.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    private final Long tokenLifetime;
    private final Key sighKey;

    public JwtTokenService(
            @Value("${jwt.lifetime}") Long tokenLifetime,
            @Value("${jwt.sign-key}") String sighKey) {
        this.tokenLifetime = tokenLifetime;
        this.sighKey = new SecretKeySpec(
                sighKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public String generateToken(UserEntity user){
        Date issuedTime = new Date();
        Date expirationTime = new Date(issuedTime.getTime() + tokenLifetime);
        return Jwts.builder()
                .subject(user.getLogin())
                .expiration(expirationTime)
                .issuedAt(issuedTime)
                .signWith(sighKey)
                .compact();
    }

    public boolean isTokenValid(String jwtToken){
        try{
            Jwts.parser()
                    .setSigningKey(sighKey)
                    .build()
                    .parse(jwtToken);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public String getLoginFromToken(String jwtToken){
        return Jwts.parser()
                .setSigningKey(sighKey)
                .build().parseClaimsJws(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwtToken){
        return Jwts.parser()
                .setSigningKey(sighKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getPayload()
                .get("role", String.class);
    }

    public Instant getExpiration(String jwtToken){
        return Jwts.parser()
                .setSigningKey(sighKey)
                .build().parseClaimsJws(jwtToken)
                .getPayload().getExpiration().toInstant();
    }

}
