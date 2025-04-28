package edu.cit.SKyber.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cit.SKyber.Entity.SKProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class SKProfileService {
    private static final Logger logger = Logger.getLogger(SKProfileService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create SK profile
    public SKProfile createSKProfile(SKProfile skProfile) {
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("firstName", skProfile.getFirstName());
            profileMap.put("lastName", skProfile.getLastName());
            profileMap.put("email", skProfile.getEmail());
            profileMap.put("position", skProfile.getPosition());
            profileMap.put("term", skProfile.getTerm());
            profileMap.put("platform", skProfile.getPlatform());
            profileMap.put("birthdate", skProfile.getBirthdate());
            profileMap.put("gender", skProfile.getGender());
            profileMap.put("age", skProfile.getAge());
            profileMap.put("phoneNumber", skProfile.getPhoneNumber());
            profileMap.put("address", skProfile.getAddress());
            profileMap.put("role", skProfile.getRole());
            
            // Store image if available
            if (skProfile.getSkImage() != null && !skProfile.getSkImage().isEmpty()) {
                profileMap.put("skImage", skProfile.getSkImage());
            }
            
            // Use provided UID or generate one
            String uid;
            if (skProfile.getUid() != null && !skProfile.getUid().isEmpty()) {
                uid = skProfile.getUid();
                // Store to Firebase at specific UID
                DatabaseReference profileRef = firebaseDatabase.getReference("SKProfiles").child(uid);
                profileRef.setValueAsync(profileMap);
            } else {
                // Generate a new key and use that as the UID
                DatabaseReference profilesRef = firebaseDatabase.getReference("SKProfiles").push();
                uid = profilesRef.getKey();
                profilesRef.setValueAsync(profileMap);
            }
            
            // Set the UID in the map and return object
            profileMap.put("uid", uid);
            skProfile.setUid(uid);
            
            logger.info("SK Profile created with UID: " + uid);
            return skProfile;
        } catch (Exception e) {
            logger.severe("Error creating SK profile: " + e.getMessage());
            throw e;
        }
    }
    
    // Get all SK profiles
    public List<SKProfile> getAllSKProfiles() {
        CompletableFuture<List<SKProfile>> future = new CompletableFuture<>();
        List<SKProfile> profiles = new ArrayList<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("SKProfiles");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String uid = childSnapshot.getKey();
                        Map<String, Object> data = (Map<String, Object>) childSnapshot.getValue();
                        
                        SKProfile profile = new SKProfile();
                        profile.setUid(uid);
                        profile.setFirstName((String) data.get("firstName"));
                        profile.setLastName((String) data.get("lastName"));
                        profile.setEmail((String) data.get("email"));
                        profile.setPosition((String) data.get("position"));
                        profile.setTerm((String) data.get("term"));
                        profile.setPlatform((String) data.get("platform"));
                        profile.setBirthdate((String) data.get("birthdate"));
                        profile.setGender((String) data.get("gender"));
                        profile.setSkImage((String) data.get("skImage"));
                        
                        // Handle age (integer field)
                        if (data.get("age") != null) {
                            if (data.get("age") instanceof Long) {
                                profile.setAge(((Long) data.get("age")).intValue());
                            } else if (data.get("age") instanceof Integer) {
                                profile.setAge((Integer) data.get("age"));
                            } else if (data.get("age") instanceof String) {
                                try {
                                    profile.setAge(Integer.parseInt((String) data.get("age")));
                                } catch (NumberFormatException e) {
                                    logger.warning("Could not parse age for profile " + uid);
                                    profile.setAge(0); // Default value
                                }
                            }
                        }
                        
                        profile.setPhoneNumber((String) data.get("phoneNumber"));
                        profile.setAddress((String) data.get("address"));
                        
                        // Set role with default if not present
                        String role = (String) data.get("role");
                        profile.setRole(role != null ? role : "ADMIN");
                        
                        profiles.add(profile);
                    }
                }
                future.complete(profiles);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error retrieving SK profiles: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Get SK profile by UID
    public SKProfile getSKProfileByUid(String uid) {
        CompletableFuture<SKProfile> future = new CompletableFuture<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("SKProfiles").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    
                    SKProfile profile = new SKProfile();
                    profile.setUid(snapshot.getKey());
                    profile.setFirstName((String) data.get("firstName"));
                    profile.setLastName((String) data.get("lastName"));
                    profile.setEmail((String) data.get("email"));
                    profile.setPosition((String) data.get("position"));
                    profile.setTerm((String) data.get("term"));
                    profile.setPlatform((String) data.get("platform"));
                    profile.setBirthdate((String) data.get("birthdate"));
                    profile.setGender((String) data.get("gender"));
                    profile.setSkImage((String) data.get("skImage"));
                    
                    // Handle age (integer field)
                    if (data.get("age") != null) {
                        if (data.get("age") instanceof Long) {
                            profile.setAge(((Long) data.get("age")).intValue());
                        } else if (data.get("age") instanceof Integer) {
                            profile.setAge((Integer) data.get("age"));
                        } else if (data.get("age") instanceof String) {
                            try {
                                profile.setAge(Integer.parseInt((String) data.get("age")));
                            } catch (NumberFormatException e) {
                                logger.warning("Could not parse age for profile " + uid);
                                profile.setAge(0); // Default value
                            }
                        }
                    }
                    
                    profile.setPhoneNumber((String) data.get("phoneNumber"));
                    profile.setAddress((String) data.get("address"));
                    
                    // Set role with default if not present
                    String role = (String) data.get("role");
                    profile.setRole(role != null ? role : "ADMIN");
                    
                    future.complete(profile);
                } else {
                    future.complete(null);
                }
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error retrieving SK profile with UID " + uid + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Update SK profile
    public SKProfile updateSKProfile(String uid, SKProfile skProfileDetails) {
        try {
            SKProfile existingProfile = getSKProfileByUid(uid);
            
            if (existingProfile == null) {
                logger.warning("SK profile with UID " + uid + " not found");
                return null;
            }
            
            // Create map with updated details
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("firstName", skProfileDetails.getFirstName());
            updatedMap.put("lastName", skProfileDetails.getLastName());
            updatedMap.put("email", skProfileDetails.getEmail());
            updatedMap.put("position", skProfileDetails.getPosition());
            updatedMap.put("term", skProfileDetails.getTerm());
            updatedMap.put("platform", skProfileDetails.getPlatform());
            updatedMap.put("birthdate", skProfileDetails.getBirthdate());
            updatedMap.put("gender", skProfileDetails.getGender());
            updatedMap.put("age", skProfileDetails.getAge());
            updatedMap.put("phoneNumber", skProfileDetails.getPhoneNumber());
            updatedMap.put("address", skProfileDetails.getAddress());
            
            // Update role if provided, otherwise keep existing
            if (skProfileDetails.getRole() != null && !skProfileDetails.getRole().isEmpty()) {
                updatedMap.put("role", skProfileDetails.getRole());
            } else {
                updatedMap.put("role", existingProfile.getRole());
            }
            
            // Only update image if provided
            if (skProfileDetails.getSkImage() != null && !skProfileDetails.getSkImage().isEmpty()) {
                updatedMap.put("skImage", skProfileDetails.getSkImage());
            } else {
                updatedMap.put("skImage", existingProfile.getSkImage());
            }
            
            // Update in Firebase
            DatabaseReference profileRef = firebaseDatabase.getReference("SKProfiles").child(uid);
            profileRef.updateChildrenAsync(updatedMap);
            
            skProfileDetails.setUid(uid);
            
            // Set role from existing if not provided
            if (skProfileDetails.getRole() == null || skProfileDetails.getRole().isEmpty()) {
                skProfileDetails.setRole(existingProfile.getRole());
            }
            
            // Preserve existing image if not provided
            if (skProfileDetails.getSkImage() == null || skProfileDetails.getSkImage().isEmpty()) {
                skProfileDetails.setSkImage(existingProfile.getSkImage());
            }
            
            logger.info("SK profile with UID " + uid + " updated successfully");
            return skProfileDetails;
        } catch (Exception e) {
            logger.severe("Error updating SK profile with UID " + uid + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Update profile image
    public SKProfile updateProfileImage(String uid, MultipartFile image) throws IOException, ExecutionException, InterruptedException {
        try {
            SKProfile existingProfile = getSKProfileByUid(uid);
            
            if (existingProfile == null) {
                logger.warning("SK profile with UID " + uid + " not found");
                return null;
            }
            
            // Process and encode image
            String base64Image = encodeImage(image);
            
            // Create map with just the image update
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("skImage", base64Image);
            
            // Update in Firebase
            DatabaseReference profileRef = firebaseDatabase.getReference("SKProfiles").child(uid);
            profileRef.updateChildrenAsync(updatedMap);
            
            // Update the object
            existingProfile.setSkImage(base64Image);
            
            logger.info("SK profile image with UID " + uid + " updated successfully");
            return existingProfile;
        } catch (Exception e) {
            logger.severe("Error updating SK profile image with UID " + uid + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Delete SK profile
    public boolean deleteSKProfile(String uid) {
        try {
            SKProfile existingProfile = getSKProfileByUid(uid);
            
            if (existingProfile == null) {
                logger.warning("SK profile with UID " + uid + " not found");
                return false;
            }
            
            // Remove from Firebase
            DatabaseReference profileRef = firebaseDatabase.getReference("SKProfiles").child(uid);
            profileRef.removeValueAsync();
            
            logger.info("SK profile with UID " + uid + " deleted successfully");
            return true;
        } catch (Exception e) {
            logger.severe("Error deleting SK profile with UID " + uid + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Get SK profiles by role
    public List<SKProfile> getSKProfilesByRole(String role) {
        try {
            List<SKProfile> allProfiles = getAllSKProfiles();
            List<SKProfile> filteredProfiles = new ArrayList<>();
            
            for (SKProfile profile : allProfiles) {
                if (role.equalsIgnoreCase(profile.getRole())) {
                    filteredProfiles.add(profile);
                }
            }
            
            logger.info("Found " + filteredProfiles.size() + " profiles with role: " + role);
            return filteredProfiles;
        } catch (Exception e) {
            logger.severe("Error retrieving SK profiles by role " + role + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Helper method to encode image to base64
    private String encodeImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null;
        }
        
        byte[] imageBytes = image.getBytes();
        return java.util.Base64.getEncoder().encodeToString(imageBytes);
    }
}