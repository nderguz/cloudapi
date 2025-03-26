package diploma.cloudapi.web.controller;

import diploma.cloudapi.service.AuthenticationService;
import diploma.cloudapi.web.dto.authorization.AuthTokenResponse;
import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> authorize(
            @RequestBody AuthorizationRequest authRequest
    )
    {
        String token = authenticationService.authenticateUser(authRequest);
        return ResponseEntity.ok().body(new AuthTokenResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("auth-token") String token
    ){
        authenticationService.logout(token.substring(7));
        return ResponseEntity.ok().build();
    }
}
