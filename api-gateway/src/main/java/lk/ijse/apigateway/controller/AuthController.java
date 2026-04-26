package lk.ijse.apigateway.controller;

import lk.ijse.apigateway.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // testing purposes only
    @PostMapping("/login")
    public String getToken(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid access!");
        }
    }
}