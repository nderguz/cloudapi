package diploma.cloudapi.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/login")
    public void authorize(){

    }

    @PostMapping("/logout")
    public void logout(){

    }
}
