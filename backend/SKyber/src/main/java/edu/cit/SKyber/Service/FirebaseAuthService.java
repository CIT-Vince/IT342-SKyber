
package edu.cit.SKyber.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    // Verify Firebase ID Token
    public FirebaseToken verifyToken(String idToken) throws Exception {
        try {
            // Firebase verifies the token and returns the decoded token if valid
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (Exception e) {
            throw new Exception("Invalid Firebase token", e);
        }
    }
}
