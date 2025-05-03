package edu.cit.SKyber.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${app.firebase.database-url}")
    private String firebaseDatabaseUrl;

    @Value("${app.firebase.config-path}")
    private String firebaseKeyPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount;

        // Prefer environment variable if present (for Render)
        String firebaseConfig = System.getenv("FIREBASE_CONFIG");
        if (firebaseConfig != null && !firebaseConfig.isEmpty()) {
            serviceAccount = new java.io.ByteArrayInputStream(firebaseConfig.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } else if (firebaseKeyPath.startsWith("classpath:")) {
            // Fallback to classpath for local development
            serviceAccount = new ClassPathResource(firebaseKeyPath.replace("classpath:", "")).getInputStream();
        } else {
            throw new IllegalArgumentException("Invalid firebaseKeyPath: " + firebaseKeyPath);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseDatabaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }

        return FirebaseApp.getInstance();
    }
    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() throws IOException {
        return FirebaseDatabase.getInstance(firebaseApp());
    }
}