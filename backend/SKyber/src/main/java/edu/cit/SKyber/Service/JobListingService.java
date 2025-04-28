package edu.cit.SKyber.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cit.SKyber.Entity.JobListing;
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
public class JobListingService {
    private static final Logger logger = Logger.getLogger(JobListingService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create job listing
    public JobListing createJobListing(JobListing jobListing) {
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> jobListingMap = new HashMap<>();
            jobListingMap.put("jobTitle", jobListing.getJobTitle());
            jobListingMap.put("companyName", jobListing.getCompanyName());
            jobListingMap.put("address", jobListing.getAddress());
            jobListingMap.put("description", jobListing.getDescription());
            jobListingMap.put("applicationlink", jobListing.getApplicationlink());
            jobListingMap.put("employementType", jobListing.getEmployementType());
            
            // Store image if available
            if (jobListing.getJobImage() != null && !jobListing.getJobImage().isEmpty()) {
                jobListingMap.put("jobImage", jobListing.getJobImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference jobListingRef = firebaseDatabase.getReference("JobListings").push();
            String key = jobListingRef.getKey();
            logger.info("Generated Firebase key: " + key);
            
            // Use this key as the ID (using string ID)
            jobListingMap.put("id", key);
            jobListing.setId(key); 
            
            // Save to Firebase
            jobListingRef.setValueAsync(jobListingMap);
            
            logger.info("Job listing created with ID: " + key);
            return jobListing;
        } catch (Exception e) {
            logger.severe("Error creating job listing: " + e.getMessage());
            throw e;
        }
    }
    
    // Get all job listings
    public List<JobListing> getAllJobListings() {
        CompletableFuture<List<JobListing>> future = new CompletableFuture<>();
        List<JobListing> jobListings = new ArrayList<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("JobListings");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String, Object> data = (Map<String, Object>) childSnapshot.getValue();
                        
                        JobListing jobListing = new JobListing();
                        jobListing.setId(key);
                        jobListing.setJobTitle((String) data.get("jobTitle"));
                        jobListing.setCompanyName((String) data.get("companyName"));
                        jobListing.setAddress((String) data.get("address"));
                        jobListing.setDescription((String) data.get("description"));
                        jobListing.setApplicationlink((String) data.get("applicationlink"));
                        jobListing.setEmployementType((String) data.get("employementType"));
                        jobListing.setJobImage((String) data.get("jobImage"));
                        
                        jobListings.add(jobListing);
                    }
                }
                future.complete(jobListings);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error retrieving job listings: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Get job listing by ID
    public JobListing getJobListingById(String id) {
        CompletableFuture<JobListing> future = new CompletableFuture<>();
        
        DatabaseReference ref = firebaseDatabase.getReference("JobListings").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    
                    JobListing jobListing = new JobListing();
                    jobListing.setId(snapshot.getKey());
                    jobListing.setJobTitle((String) data.get("jobTitle"));
                    jobListing.setCompanyName((String) data.get("companyName"));
                    jobListing.setAddress((String) data.get("address"));
                    jobListing.setDescription((String) data.get("description"));
                    jobListing.setApplicationlink((String) data.get("applicationlink"));
                    jobListing.setEmployementType((String) data.get("employementType"));
                    jobListing.setJobImage((String) data.get("jobImage"));
                    
                    future.complete(jobListing);
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
            logger.severe("Error retrieving job listing with ID " + id + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }
    
    // Update job listing
    public JobListing updateJobListing(String id, JobListing jobListingDetails) {
        try {
            JobListing existingJobListing = getJobListingById(id);
            
            if (existingJobListing == null) {
                logger.warning("Job listing with ID " + id + " not found");
                return null;
            }
            
            // Create map with updated details
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("id", id);
            updatedMap.put("jobTitle", jobListingDetails.getJobTitle());
            updatedMap.put("companyName", jobListingDetails.getCompanyName());
            updatedMap.put("address", jobListingDetails.getAddress());
            updatedMap.put("description", jobListingDetails.getDescription());
            updatedMap.put("applicationlink", jobListingDetails.getApplicationlink());
            updatedMap.put("employementType", jobListingDetails.getEmployementType());
            
            // Only update image if provided
            if (jobListingDetails.getJobImage() != null && !jobListingDetails.getJobImage().isEmpty()) {
                updatedMap.put("jobImage", jobListingDetails.getJobImage());
            } else {
                updatedMap.put("jobImage", existingJobListing.getJobImage());
            }
            
            // Update in Firebase
            DatabaseReference jobListingRef = firebaseDatabase.getReference("JobListings").child(id);
            jobListingRef.updateChildrenAsync(updatedMap);
            
            jobListingDetails.setId(id);
            
            if (jobListingDetails.getJobImage() == null || jobListingDetails.getJobImage().isEmpty()) {
                jobListingDetails.setJobImage(existingJobListing.getJobImage());
            }
            
            logger.info("Job listing with ID " + id + " updated successfully");
            return jobListingDetails;
        } catch (Exception e) {
            logger.severe("Error updating job listing with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Update job listing image
    public JobListing updateJobImage(String id, MultipartFile image) throws IOException, ExecutionException, InterruptedException {
        try {
            JobListing existingJobListing = getJobListingById(id);
            
            if (existingJobListing == null) {
                logger.warning("Job listing with ID " + id + " not found");
                return null;
            }
            
            // Process and encode image
            String base64Image = encodeImage(image);
            
            // Create map with just the image update
            Map<String, Object> updatedMap = new HashMap<>();
            updatedMap.put("jobImage", base64Image);
            
            // Update in Firebase
            DatabaseReference jobListingRef = firebaseDatabase.getReference("JobListings").child(id);
            jobListingRef.updateChildrenAsync(updatedMap);
            
            // Update the object
            existingJobListing.setJobImage(base64Image);
            
            logger.info("Job listing image with ID " + id + " updated successfully");
            return existingJobListing;
        } catch (Exception e) {
            logger.severe("Error updating job listing image with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Delete job listing
    public boolean deleteJobListing(String id) {
        try {
            JobListing existingJobListing = getJobListingById(id);
            
            if (existingJobListing == null) {
                logger.warning("Job listing with ID " + id + " not found");
                return false;
            }
            
            // Remove from Firebase
            DatabaseReference jobListingRef = firebaseDatabase.getReference("JobListings").child(id);
            jobListingRef.removeValueAsync();
            
            logger.info("Job listing with ID " + id + " deleted successfully");
            return true;
        } catch (Exception e) {
            logger.severe("Error deleting job listing with ID " + id + ": " + e.getMessage());
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