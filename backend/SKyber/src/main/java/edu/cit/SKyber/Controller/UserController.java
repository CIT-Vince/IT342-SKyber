package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.AuthRequest;
import edu.cit.SKyber.Entity.UserInfo;
import edu.cit.SKyber.Service.JwtService;
import edu.cit.SKyber.Service.UserInfoService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserInfo userInfo) {
    String result = service.addUser(userInfo);
    
    Map<String, String> response = new HashMap<>();
    if (result.equals("User added successfully!")) {
        response.put("message", result);  // Adding message to the response body
        return ResponseEntity.ok(response);  // 200 OK on success
    } else {
        response.put("message", "Error adding user.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400 Bad Request on failure
    }
}

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        return service.loginUserAndGenerateToken(email, password); // Authenticate and generate JWT token
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
