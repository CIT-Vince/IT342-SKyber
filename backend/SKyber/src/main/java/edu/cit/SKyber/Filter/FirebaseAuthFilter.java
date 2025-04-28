package edu.cit.SKyber.Filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
@WebFilter("/*")
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String idToken = request.getHeader("Authorization");

        if (idToken != null && idToken.startsWith("Bearer ")) {
            try {
                // Extract token from header
                idToken = idToken.replace("Bearer ", "");
                logger.info("Token received: {}", idToken);

                // Verify the token with Firebase
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();
                logger.info("Decoded UID: {}", uid);

                // If valid, authenticate using UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uid, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication token in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (FirebaseAuthException e) {
                // Handle invalid Firebase token
                logger.error("Invalid or expired Firebase token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired Firebase token.");
                return;
            }
        } else {
            logger.warn("No Authorization header found or Bearer token missing.");
        }

        filterChain.doFilter(request, response);  // Proceed with the filter chain
    }
}
