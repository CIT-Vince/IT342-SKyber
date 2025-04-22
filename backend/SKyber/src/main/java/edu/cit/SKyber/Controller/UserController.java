package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.UserInfo;
<<<<<<< HEAD
import edu.cit.SKyber.Service.UserInfoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
=======
import edu.cit.SKyber.Entity.AuthRequest;
import edu.cit.SKyber.Service.JwtService;
import edu.cit.SKyber.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

<<<<<<< HEAD
    // Register a new user
    @PostMapping("/register")
    public String registerUser(@RequestBody UserInfo userInfo) {
        return userInfoService.registerUser(userInfo);
    }

    // Get user info by UID
    @GetMapping("/get/{uid}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String uid) {
        try {
            UserInfo userInfo = userInfoService.getUserInfo(uid);
            return ResponseEntity.ok(userInfo);
        } catch (InterruptedException e) {
            // Handle interruption
            Thread.currentThread().interrupt();  // Restore the interrupted status
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body(null); // Or you can return a custom error message
        }
    }

    // Update user info by UID
    @PutMapping("/update/{uid}")
    public String updateUserInfo(@PathVariable String uid, @RequestBody UserInfo updatedUserInfo) {
        return userInfoService.updateUserInfo(uid, updatedUserInfo);
    }

    // Delete user by UID
    @DeleteMapping("/delete/{uid}")
    public String deleteUser(@PathVariable String uid) {
        return userInfoService.deleteUser(uid);
    }

    // Login with Firebase ID token
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestHeader("Authorization") String token) {
        try {
            // Extract the ID token from the Authorization header
            String idToken = token.replace("Bearer ", "");

            // Verify the Firebase ID token using Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Fetch the user details from your system based on UID
            UserInfo userInfo = userInfoService.getUserInfo(uid);

            // Respond with user data
            return ResponseEntity.ok(userInfo); // Or you can send a JWT token for session management

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Invalid or expired Firebase token");
        }
    }
=======
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    // Removed the role checks here as they are already managed in SecurityConfig

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
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
}