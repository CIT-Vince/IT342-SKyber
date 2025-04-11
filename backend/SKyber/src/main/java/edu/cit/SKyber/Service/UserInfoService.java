package edu.cit.SKyber.Service;

import edu.cit.SKyber.Entity.UserInfo;
import edu.cit.SKyber.Repository.UserInfoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder, JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        // Check if the email already exists
        Optional<UserInfo> existingUser = repository.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent()) {
            return "User with this email already exists!";
        }
    
        // Encrypt the password before saving
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added successfully!";
    }
    

    public String loginUserAndGenerateToken(String email, String password) {
        Optional<UserInfo> userInfo = repository.findByEmail(email);
        if (userInfo.isEmpty()) {
            return "User not found!";
        }

        if (encoder.matches(password, userInfo.get().getPassword())) {
            // Password matches, generate and return JWT token
            return jwtService.generateToken(email);
        }
        return "Invalid credentials!";
    }
}
