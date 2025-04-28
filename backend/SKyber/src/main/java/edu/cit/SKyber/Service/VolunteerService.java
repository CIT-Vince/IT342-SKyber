package edu.cit.SKyber.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cit.SKyber.Entity.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class VolunteerService {
    private static final Logger logger = Logger.getLogger(VolunteerService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create volunteer opportunity
    public Volunteer createVolunteer(Volunteer volunteer) {
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> volunteerMap = new HashMap<>();
            volunteerMap.put("title", volunteer.getTitle());
            volunteerMap.put("description", volunteer.getDescription());
            volunteerMap.put("registerLink", volunteer.getRegisterLink());
            volunteerMap.put("category", volunteer.getCategory());
            volunteerMap.put("location", volunteer.getLocation());
            volunteerMap.put("contactPerson", volunteer.getContactPerson());
            volunteerMap.put("contactEmail", volunteer.getContactEmail());
            volunteerMap.put("status", volunteer.getStatus());
            volunteerMap.put("requirements", volunteer.getRequirements());
            
            // Handle date - convert to string for Firebase
            if (volunteer.getEventDate() != null) {
                volunteerMap.put("eventDate", volunteer.getEventDate().toString());
            }
            
            // Store image if available
            if (volunteer.getVolunteerImage() != null && !volunteer.getVolunteerImage().isEmpty()) {
                volunteerMap.put("volunteerImage", volunteer.getVolunteerImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference volunteerRef = firebaseDatabase.getReference("Volunteers").push();
            String key = volunteerRef.getKey();
            logger.info("Generated Firebase key: " + key);
            
            // Use this key as the ID (using string ID)
            volunteerMap.put("id", key);
            volunteer.setId(key); 
            
            // Save to Firebase
            volunteerRef.setValueAsync(volunteerMap);
            
            logger.info("Volunteer opportunity created with ID: " + key);
            return volunteer;
        } catch (Exception e) {
            logger.severe("Error creating volunteer opportunity: " + e.getMessage());
            throw e;
        }
    }
    
    // Get all volunteer opportunities
    public List<Volunteer> getAllVolunteers() {
        CompletableFuture<List<Volunteer>> future = new CompletableFuture<>();
        List<Volunteer> volunteers = new ArrayList<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("Volunteers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String, Object> data = (Map<String, Object>) childSnapshot.getValue();
                        
                        Volunteer volunteer = new Volunteer();
                        volunteer.setId(key);
                        volunteer.setTitle((String) data.get("title"));
                        volunteer.setDescription((String) data.get("description"));
                        volunteer.setRegisterLink((String) data.get("registerLink"));
                        volunteer.setCategory((String) data.get("category"));
                        volunteer.setLocation((String) data.get("location"));
                        volunteer.setContactPerson((String) data.get("contactPerson"));
                        volunteer.setContactEmail((String) data.get("contactEmail"));
                        volunteer.setStatus((String) data.get("status"));
                        volunteer.setRequirements((String) data.get("requirements"));
                        volunteer.setVolunteerImage((String) data.get("volunteerImage"));
                        
                        // Convert date string back to LocalDate
                        String dateString = (String) data.get("eventDate");
                        if (dateString != null && !dateString.isEmpty()) {
                            try {
                                volunteer.setEventDate(LocalDate.parse(dateString));
                            } catch (Exception e) {
                                logger.warning("Error parsing date: " + dateString);
                            }
                        }
                        
                        volunteers.add(volunteer);
                    }
                }
                future.complete(volunteers);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error retrieving volunteer opportunities: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Get volunteer opportunity by ID
    public Volunteer getVolunteerById(String id) {
        CompletableFuture<Volunteer> future = new CompletableFuture<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("Volunteers").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    
                    Volunteer volunteer = new Volunteer();
                    volunteer.setId(snapshot.getKey());
                    volunteer.setTitle((String) data.get("title"));
                    volunteer.setDescription((String) data.get("description"));
                    volunteer.setRegisterLink((String) data.get("registerLink"));
                    volunteer.setCategory((String) data.get("category"));
                    volunteer.setLocation((String) data.get("location"));
                    volunteer.setContactPerson((String) data.get("contactPerson"));
                    volunteer.setContactEmail((String) data.get("contactEmail"));
                    volunteer.setStatus((String) data.get("status"));
                    volunteer.setRequirements((String) data.get("requirements"));
                    volunteer.setVolunteerImage((String) data.get("volunteerImage"));
                    
                    // Convert date string back to LocalDate
                    String dateString = (String) data.get("eventDate");
                    if (dateString != null && !dateString.isEmpty()) {
                        try {
                            volunteer.setEventDate(LocalDate.parse(dateString));
                        } catch (Exception e) {
                            logger.warning("Error parsing date: " + dateString);
                        }
                    }
                    
                    future.complete(volunteer);
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
            logger.severe("Error retrieving volunteer opportunity with ID " + id + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Update volunteer opportunity
    public Volunteer updateVolunteer(String id, Volunteer volunteerDetails) {
        try {
            Volunteer existingVolunteer = getVolunteerById(id);
            
            if (existingVolunteer == null) {
                logger.warning("Volunteer opportunity with ID " + id + " not found");
                return null;
            }
            
            // Create map with updated details
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("id", id);
            updatedMap.put("title", volunteerDetails.getTitle());
            updatedMap.put("description", volunteerDetails.getDescription());
            updatedMap.put("registerLink", volunteerDetails.getRegisterLink());
            updatedMap.put("category", volunteerDetails.getCategory());
            updatedMap.put("location", volunteerDetails.getLocation());
            updatedMap.put("contactPerson", volunteerDetails.getContactPerson());
            updatedMap.put("contactEmail", volunteerDetails.getContactEmail());
            updatedMap.put("status", volunteerDetails.getStatus());
            updatedMap.put("requirements", volunteerDetails.getRequirements());
            
            // Handle date - convert to string for Firebase
            if (volunteerDetails.getEventDate() != null) {
                updatedMap.put("eventDate", volunteerDetails.getEventDate().toString());
            } else if (existingVolunteer.getEventDate() != null) {
                updatedMap.put("eventDate", existingVolunteer.getEventDate().toString());
            }
            
            // Only update image if provided
            if (volunteerDetails.getVolunteerImage() != null && !volunteerDetails.getVolunteerImage().isEmpty()) {
                updatedMap.put("volunteerImage", volunteerDetails.getVolunteerImage());
            } else {
                updatedMap.put("volunteerImage", existingVolunteer.getVolunteerImage());
            }
            
            // Update in Firebase
            DatabaseReference volunteerRef = firebaseDatabase.getReference("Volunteers").child(id);
            volunteerRef.updateChildrenAsync(updatedMap);
            
            volunteerDetails.setId(id);
            
            if (volunteerDetails.getVolunteerImage() == null || volunteerDetails.getVolunteerImage().isEmpty()) {
                volunteerDetails.setVolunteerImage(existingVolunteer.getVolunteerImage());
            }
            
            // Ensure we preserve the event date if not set in update
            if (volunteerDetails.getEventDate() == null && existingVolunteer.getEventDate() != null) {
                volunteerDetails.setEventDate(existingVolunteer.getEventDate());
            }
            
            logger.info("Volunteer opportunity with ID " + id + " updated successfully");
            return volunteerDetails;
        } catch (Exception e) {
            logger.severe("Error updating volunteer opportunity with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Update volunteer image
    public Volunteer updateVolunteerImage(String id, MultipartFile image) throws IOException, ExecutionException, InterruptedException {
        try {
            Volunteer existingVolunteer = getVolunteerById(id);
            
            if (existingVolunteer == null) {
                logger.warning("Volunteer opportunity with ID " + id + " not found");
                return null;
            }
            
            // Process and encode image
            String base64Image = encodeImage(image);
            
            // Create map with just the image update
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("volunteerImage", base64Image);
            
            // Update in Firebase
            DatabaseReference volunteerRef = firebaseDatabase.getReference("Volunteers").child(id);
            volunteerRef.updateChildrenAsync(updatedMap);
            
            // Update the object
            existingVolunteer.setVolunteerImage(base64Image);
            
            logger.info("Volunteer image with ID " + id + " updated successfully");
            return existingVolunteer;
        } catch (Exception e) {
            logger.severe("Error updating volunteer image with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Delete volunteer opportunity
    public boolean deleteVolunteer(String id) {
        try {
            Volunteer existingVolunteer = getVolunteerById(id);
            
            if (existingVolunteer == null) {
                logger.warning("Volunteer opportunity with ID " + id + " not found");
                return false;
            }
            
            // Remove from Firebase
            DatabaseReference volunteerRef = firebaseDatabase.getReference("Volunteers").child(id);
            volunteerRef.removeValueAsync();
            
            logger.info("Volunteer opportunity with ID " + id + " deleted successfully");
            return true;
        } catch (Exception e) {
            logger.severe("Error deleting volunteer opportunity with ID " + id + ": " + e.getMessage());
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