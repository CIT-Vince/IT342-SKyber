package edu.cit.SKyber.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.*;
import edu.cit.SKyber.Entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class UserInfoService {

<<<<<<< HEAD
    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Register a new user (for adding extra data)
    public String registerUser(UserInfo userInfo) {
        try {
            // You could generate Firebase UID here if user is already authenticated
            // Add user info to Firebase Realtime Database
            DatabaseReference userRef = firebaseDatabase.getReference("users").child(userInfo.getUid());
            userRef.setValueAsync(userInfo);
            return "User registered successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Error registering user", e);
        }
=======
    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    // Constructor-based dependency injection
    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
    }

    // Get user info from Firebase by UID
    public UserInfo getUserInfo(String uid) throws InterruptedException {
        // Create a latch for waiting asynchronously
        final CountDownLatch latch = new CountDownLatch(1);
        final UserInfo[] userInfo = new UserInfo[1];

        DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);
        
        // Listen for value changes, asynchronous
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get the UserInfo object from the snapshot
                userInfo[0] = snapshot.getValue(UserInfo.class);
                latch.countDown(); // Release the latch when done
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown(); // In case of error, release latch
            }
        });

        latch.await(); // Wait for the operation to complete

        return userInfo[0]; // Return the UserInfo object
    }

<<<<<<< HEAD
    // Update user info in Firebase
    public String updateUserInfo(String uid, UserInfo updatedUserInfo) {
        try {
            // Get a reference to the user's node in the database
            DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);

            // Update the user's information
            userRef.setValueAsync(updatedUserInfo);
            return "User info updated successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Error updating user info", e);
        }
    }

    // Delete user info from Firebase by UID
    public String deleteUser(String uid) {
        try {
            DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);
            userRef.removeValueAsync();
            return "User deleted successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    // Verify user using Firebase token (authentication)
    public FirebaseToken verifyToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken); // Verify Firebase token
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
=======
    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
    }
}
