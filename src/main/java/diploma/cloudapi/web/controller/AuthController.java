package diploma.cloudapi.web.controller;

import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.service.AuthenticationService;
import diploma.cloudapi.web.dto.AuthTokenResponse;
import diploma.cloudapi.web.dto.AuthorizationRequest;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cloud")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authorize(
            @RequestBody AuthorizationRequest authRequest
    )
    {
        String token = authenticationService.authenticateUser(authRequest);
        return ResponseEntity
                .ok()
                .body(new AuthTokenResponse(token));
    }

    @PostMapping("/logout")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void logout(@AuthenticationPrincipal UserEntity user){

    }
}
