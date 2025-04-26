package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.UserInfo;
import edu.cit.SKyber.Service.UserInfoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

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
}