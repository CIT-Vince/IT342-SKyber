package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.SKProfile;
import edu.cit.SKyber.Service.SKProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/profiles")
public class SKProfileController {
    private static final Logger logger = Logger.getLogger(SKProfileController.class.getName());
    
    @Autowired
    private SKProfileService skProfileService;
    
    // Create SK profile
    @PostMapping("/createProfile")
    public ResponseEntity<SKProfile> createSKProfile(@RequestBody SKProfile skProfile) {
        try {
            logger.info("Request received to create SK profile");
            SKProfile createdProfile = skProfileService.createSKProfile(skProfile);
            return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating SK profile: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create SK profile with image
    @PostMapping("/createProfile/with-image")
    public ResponseEntity<SKProfile> createSKProfileWithImage(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "term", required = false) String term,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "birthdate", required = false) String birthdate,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "age", required = false, defaultValue = "0") int age,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "role", required = false, defaultValue = "ADMIN") String role,
            @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to create SK profile with image");
            
            SKProfile skProfile = new SKProfile();
            skProfile.setFirstName(firstName);
            skProfile.setLastName(lastName);
            skProfile.setEmail(email);
            skProfile.setPosition(position);
            skProfile.setTerm(term);
            skProfile.setPlatform(platform);
            skProfile.setBirthdate(birthdate);
            skProfile.setGender(gender);
            skProfile.setAge(age);
            skProfile.setPhoneNumber(phoneNumber);
            skProfile.setAddress(address);
            skProfile.setRole(role);
            
            // Process image if provided
            if (image != null && !image.isEmpty()) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                skProfile.setSkImage(base64Image);
            }
            
            SKProfile createdProfile = skProfileService.createSKProfile(skProfile);
            return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating SK profile with image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all SK profiles
    @GetMapping("/getAllProfiles")
    public ResponseEntity<List<SKProfile>> getAllSKProfiles() {
        try {
            logger.info("Request received to get all SK profiles");
            List<SKProfile> profiles = skProfileService.getAllSKProfiles();
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving SK profiles: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get SK profile by UID
    @GetMapping("/getProfile/{uid}")
    public ResponseEntity<SKProfile> getSKProfileByUid(@PathVariable("uid") String uid) {
        try {
            logger.info("Request received to get SK profile with UID: " + uid);
            SKProfile profile = skProfileService.getSKProfileByUid(uid);
            
            if (profile != null) {
                return new ResponseEntity<>(profile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error retrieving SK profile with UID " + uid + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update SK profile
    @PutMapping("/updateProfile/{uid}")
    public ResponseEntity<SKProfile> updateSKProfile(@PathVariable("uid") String uid, @RequestBody SKProfile skProfile) {
        try {
            logger.info("Request received to update SK profile with UID: " + uid);
            SKProfile updatedProfile = skProfileService.updateSKProfile(uid, skProfile);
            
            if (updatedProfile != null) {
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error updating SK profile with UID " + uid + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update profile image
    @PutMapping("/updateProfile/{uid}/image")
    public ResponseEntity<SKProfile> updateProfileImage(@PathVariable("uid") String uid, @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to update image for SK profile with UID: " + uid);
            SKProfile updatedProfile = skProfileService.updateProfileImage(uid, image);
            
            if (updatedProfile != null) {
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Error updating SK profile image with UID " + uid + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.severe("Error updating SK profile image with UID " + uid + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete SK profile
    @DeleteMapping("/deleteProfile/{uid}")
    public ResponseEntity<HttpStatus> deleteSKProfile(@PathVariable("uid") String uid) {
        try {
            logger.info("Request received to delete SK profile with UID: " + uid);
            boolean deleted = skProfileService.deleteSKProfile(uid);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error deleting SK profile with UID " + uid + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get SK profiles by role
    @GetMapping("/getProfilesByRole")
    public ResponseEntity<List<SKProfile>> getSKProfilesByRole(@RequestParam("role") String role) {
        try {
            logger.info("Request received to get SK profiles with role: " + role);
            List<SKProfile> profiles = skProfileService.getSKProfilesByRole(role);
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving SK profiles by role " + role + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("SK Profile controller is working");
    }
}