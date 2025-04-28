package edu.cit.SKyber.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cit.SKyber.Entity.Scholarship;
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
public class ScholarshipService {
    private static final Logger logger = Logger.getLogger(ScholarshipService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create scholarship
    public Scholarship createScholarship(Scholarship scholarship) {
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> scholarshipMap = new HashMap<>();
            scholarshipMap.put("title", scholarship.getTitle());
            scholarshipMap.put("description", scholarship.getDescription());
            scholarshipMap.put("link", scholarship.getLink());
            scholarshipMap.put("contactEmail", scholarship.getContactEmail());
            scholarshipMap.put("type", scholarship.getType());
            
            // Store image if available
            if (scholarship.getScholarImage() != null && !scholarship.getScholarImage().isEmpty()) {
                scholarshipMap.put("scholarImage", scholarship.getScholarImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference scholarshipRef = firebaseDatabase.getReference("Scholarships").push();
            String key = scholarshipRef.getKey();
            logger.info("Generated Firebase key: " + key);
            
            // Use this key as the ID (using string ID)
            scholarshipMap.put("id", key);
            scholarship.setId(key); 
            
            // Save to Firebase
            scholarshipRef.setValueAsync(scholarshipMap);
            
            logger.info("Scholarship created with ID: " + key);
            return scholarship;
        } catch (Exception e) {
            logger.severe("Error creating scholarship: " + e.getMessage());
            throw e;
        }
    }
    
    // Get all scholarships
    public List<Scholarship> getAllScholarships() {
        CompletableFuture<List<Scholarship>> future = new CompletableFuture<>();
        List<Scholarship> scholarships = new ArrayList<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("Scholarships");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String, Object> data = (Map<String, Object>) childSnapshot.getValue();
                        
                        Scholarship scholarship = new Scholarship();
                        scholarship.setId(key);
                        scholarship.setTitle((String) data.get("title"));
                        scholarship.setDescription((String) data.get("description"));
                        scholarship.setLink((String) data.get("link"));
                        scholarship.setContactEmail((String) data.get("contactEmail"));
                        scholarship.setType((String) data.get("type"));
                        scholarship.setScholarImage((String) data.get("scholarImage"));
                        
                        scholarships.add(scholarship);
                    }
                }
                future.complete(scholarships);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error retrieving scholarships: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Get scholarship by ID
    public Scholarship getScholarshipById(String id) {
        CompletableFuture<Scholarship> future = new CompletableFuture<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("Scholarships").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    
                    Scholarship scholarship = new Scholarship();
                    scholarship.setId(snapshot.getKey());
                    scholarship.setTitle((String) data.get("title"));
                    scholarship.setDescription((String) data.get("description"));
                    scholarship.setLink((String) data.get("link"));
                    scholarship.setContactEmail((String) data.get("contactEmail"));
                    scholarship.setType((String) data.get("type"));
                    scholarship.setScholarImage((String) data.get("scholarImage"));
                    
                    future.complete(scholarship);
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
            logger.severe("Error retrieving scholarship with ID " + id + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Update scholarship
    public Scholarship updateScholarship(String id, Scholarship scholarshipDetails) {
        try {
            Scholarship existingScholarship = getScholarshipById(id);
            
            if (existingScholarship == null) {
                logger.warning("Scholarship with ID " + id + " not found");
                return null;
            }
            
            // Create map with updated details
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("id", id);
            updatedMap.put("title", scholarshipDetails.getTitle());
            updatedMap.put("description", scholarshipDetails.getDescription());
            updatedMap.put("link", scholarshipDetails.getLink());
            updatedMap.put("contactEmail", scholarshipDetails.getContactEmail());
            updatedMap.put("type", scholarshipDetails.getType());
            
            // Only update image if provided
            if (scholarshipDetails.getScholarImage() != null && !scholarshipDetails.getScholarImage().isEmpty()) {
                updatedMap.put("scholarImage", scholarshipDetails.getScholarImage());
            } else {
                updatedMap.put("scholarImage", existingScholarship.getScholarImage());
            }
            
            // Update in Firebase
            DatabaseReference scholarshipRef = firebaseDatabase.getReference("Scholarships").child(id);
            scholarshipRef.updateChildrenAsync(updatedMap);
            
            scholarshipDetails.setId(id);
            
            if (scholarshipDetails.getScholarImage() == null || scholarshipDetails.getScholarImage().isEmpty()) {
                scholarshipDetails.setScholarImage(existingScholarship.getScholarImage());
            }
            
            logger.info("Scholarship with ID " + id + " updated successfully");
            return scholarshipDetails;
        } catch (Exception e) {
            logger.severe("Error updating scholarship with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Update scholarship image
    public Scholarship updateScholarshipImage(String id, MultipartFile image) throws IOException, ExecutionException, InterruptedException {
        try {
            Scholarship existingScholarship = getScholarshipById(id);
            
            if (existingScholarship == null) {
                logger.warning("Scholarship with ID " + id + " not found");
                return null;
            }
            
            // Process and encode image
            String base64Image = encodeImage(image);
            
            // Create map with just the image update
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("scholarImage", base64Image);
            
            // Update in Firebase
            DatabaseReference scholarshipRef = firebaseDatabase.getReference("Scholarships").child(id);
            scholarshipRef.updateChildrenAsync(updatedMap);
            
            // Update the object
            existingScholarship.setScholarImage(base64Image);
            
            logger.info("Scholarship image with ID " + id + " updated successfully");
            return existingScholarship;
        } catch (Exception e) {
            logger.severe("Error updating scholarship image with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Delete scholarship
    public boolean deleteScholarship(String id) {
        try {
            Scholarship existingScholarship = getScholarshipById(id);
            
            if (existingScholarship == null) {
                logger.warning("Scholarship with ID " + id + " not found");
                return false;
            }
            
            // Remove from Firebase
            DatabaseReference scholarshipRef = firebaseDatabase.getReference("Scholarships").child(id);
            scholarshipRef.removeValueAsync();
            
            logger.info("Scholarship with ID " + id + " deleted successfully");
            return true;
        } catch (Exception e) {
            logger.severe("Error deleting scholarship with ID " + id + ": " + e.getMessage());
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