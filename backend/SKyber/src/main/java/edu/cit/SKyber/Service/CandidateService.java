package edu.cit.SKyber.Service;

import com.google.firebase.database.*;
import edu.cit.SKyber.Entity.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class CandidateService {
    
    private static final Logger logger = Logger.getLogger(CandidateService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;
    
    // Create a new candidate
    public Candidate createCandidate(Candidate candidate) {
        try {
            logger.info("Creating new candidate: " + candidate.getFirstName() + " " + candidate.getLastName());
            
            // Create a map for Firebase compatibility
            Map<String, Object> candidateMap = new HashMap<>();
            candidateMap.put("firstName", candidate.getFirstName());
            candidateMap.put("lastName", candidate.getLastName());
            candidateMap.put("age", candidate.getAge());
            candidateMap.put("address", candidate.getAddress());
            candidateMap.put("partylist", candidate.getPartylist());
            candidateMap.put("platform", candidate.getPlatform());
            
            // Store image if available
            if (candidate.getCandidateImage() != null && !candidate.getCandidateImage().isEmpty()) {
                candidateMap.put("candidateImage", candidate.getCandidateImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference candidateRef = firebaseDatabase.getReference("Candidates").push();
            String key = candidateRef.getKey();
            logger.info("Generated Firebase key: " + key);
            
            // Use this SAME key as the ID
            candidateMap.put("id", key);
            candidate.setId(key); // Better yet, use the string ID directly
            
            // Save to Firebase
            candidateRef.setValueAsync(candidateMap);
            
            logger.info("Candidate created with ID: " + key);
            return candidate;
            
        } catch (Exception e) {
            logger.severe("Error creating candidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating candidate", e);
        }
    }
    
    // Create candidate with image
    public Candidate createCandidateWithImage(
            String firstName, 
            String lastName, 
            String age, 
            String address,
            String partylist,
            String platform,
            MultipartFile image) throws IOException {
        
        logger.info("Creating candidate with image: " + firstName + " " + lastName);
        Candidate candidate = new Candidate();
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setAge(age);
        candidate.setAddress(address);
        candidate.setPartylist(partylist);
        candidate.setPlatform(platform);
        
        if (image != null && !image.isEmpty()) {
            // Convert MultipartFile to Base64 encoded string
            candidate.setCandidateImage(Base64.getEncoder().encodeToString(image.getBytes()));
            logger.info("Image attached to candidate");
        }
        
        return createCandidate(candidate);
    }

    // Get all candidates
    public List<Candidate> getAllCandidates() throws InterruptedException {
        logger.info("Fetching all candidates");
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Candidate> candidates = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference candidatesRef = firebaseDatabase.getReference("Candidates");
            candidatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        logger.info("Retrieved " + snapshot.getChildrenCount() + " candidates from Firebase");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                logger.info("Processing candidate with key: " + key);
                                
                                // Create a new candidate
                                Candidate candidate = new Candidate();
                                
                                // Use the Firebase key's hashCode for numeric ID
                                candidate.setId(key);
                                
                                // Map fields from Firebase to entity
                                if (dataSnapshot.hasChild("firstName")) {
                                    candidate.setFirstName(dataSnapshot.child("firstName").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("lastName")) {
                                    candidate.setLastName(dataSnapshot.child("lastName").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("age")) {
                                    candidate.setAge(dataSnapshot.child("age").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("address")) {
                                    candidate.setAddress(dataSnapshot.child("address").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("partylist")) {
                                    candidate.setPartylist(dataSnapshot.child("partylist").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("platform")) {
                                    candidate.setPlatform(dataSnapshot.child("platform").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("candidateImage")) {
                                    candidate.setCandidateImage(dataSnapshot.child("candidateImage").getValue(String.class));
                                }
                                
                                candidates.add(candidate);
                                logger.fine("Processed candidate: " + candidate.getFirstName() + " " + candidate.getLastName());
                            } catch (Exception e) {
                                logger.warning("Error processing candidate: " + e.getMessage());
                                e.printStackTrace();
                                // Continue to next candidate
                            }
                        }
                    } catch (Exception e) {
                        logger.severe("Error in data change processing: " + e.getMessage());
                        e.printStackTrace();
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    logger.severe("Firebase query was cancelled: " + error.getMessage());
                    exceptions.add(new RuntimeException("Firebase error: " + error.getMessage()));
                    latch.countDown();
                }
            });

            // Wait with timeout
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Firebase query timed out after 10 seconds");
            }
            
            if (!exceptions.isEmpty()) {
                throw exceptions.get(0);
            }
            
            logger.info("Successfully retrieved " + candidates.size() + " candidates");
            return candidates;
            
        } catch (Exception e) {
            logger.severe("Error getting candidates: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching candidates", e);
        }
    }

    // Get candidate by ID
    public Candidate getCandidateById(String id) throws InterruptedException {
        logger.info("Fetching candidate with ID: " + id);
        
        final CountDownLatch latch = new CountDownLatch(1);
        final Candidate[] candidate = new Candidate[1];
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference candidateRef = firebaseDatabase.getReference("Candidates").child(id);
            
            candidateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            logger.warning("No candidate found with direct ID: " + id);
                            latch.countDown();
                            return;
                        }
                        
                        String key = snapshot.getKey();
                        logger.info("Found candidate with key: " + key);
                        
                        // Create a new candidate
                        Candidate c = new Candidate();
                        c.setId(key);
                        
                        // Map fields from Firebase
                        if (snapshot.hasChild("firstName")) {
                            c.setFirstName(snapshot.child("firstName").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("lastName")) {
                            c.setLastName(snapshot.child("lastName").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("age")) {
                            c.setAge(snapshot.child("age").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("address")) {
                            c.setAddress(snapshot.child("address").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("partylist")) {
                            c.setPartylist(snapshot.child("partylist").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("platform")) {
                            c.setPlatform(snapshot.child("platform").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("candidateImage")) {
                            c.setCandidateImage(snapshot.child("candidateImage").getValue(String.class));
                        }
                        
                        candidate[0] = c;
                    } catch (Exception e) {
                        logger.severe("Error processing candidate data: " + e.getMessage());
                        e.printStackTrace();
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    logger.severe("Firebase query was cancelled: " + error.getMessage());
                    exceptions.add(new RuntimeException("Firebase error: " + error.getMessage()));
                    latch.countDown();
                }
            });

            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Firebase query timed out after 10 seconds");
            }
            
            if (!exceptions.isEmpty()) {
                throw exceptions.get(0);
            }
            
            if (candidate[0] == null) {
                logger.severe("Candidate not found with ID: " + id);
                throw new RuntimeException("Candidate not found with ID: " + id);
            }
            
            return candidate[0];
            
        } catch (Exception e) {
            logger.severe("Error getting candidate by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching candidate by ID", e);
        }
    }

    // Update candidate
    public Candidate updateCandidate(String id, Candidate candidateDetails) {
        logger.info("Updating candidate with ID: " + id);
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> updates = new HashMap<>();
            
            // Only update fields that are provided
            if (candidateDetails.getFirstName() != null) {
                updates.put("firstName", candidateDetails.getFirstName());
            }
            
            if (candidateDetails.getLastName() != null) {
                updates.put("lastName", candidateDetails.getLastName());
            }
            
            if (candidateDetails.getAge() != null) {
                updates.put("age", candidateDetails.getAge());
            }
            
            if (candidateDetails.getAddress() != null) {
                updates.put("address", candidateDetails.getAddress());
            }
            
            if (candidateDetails.getPartylist() != null) {
                updates.put("partylist", candidateDetails.getPartylist());
            }
            
            if (candidateDetails.getPlatform() != null) {
                updates.put("platform", candidateDetails.getPlatform());
            }
            
            if (candidateDetails.getCandidateImage() != null && !candidateDetails.getCandidateImage().isEmpty()) {
                updates.put("candidateImage", candidateDetails.getCandidateImage());
            }
            
            // Update in Firebase
            DatabaseReference candidateRef = firebaseDatabase.getReference("Candidates").child(id);
            candidateRef.updateChildrenAsync(updates);
            
            // Set the ID in the returned object for consistency
            candidateDetails.setId(id);
            
            logger.info("Candidate updated successfully");
            return candidateDetails;
            
        } catch (Exception e) {
            logger.severe("Error updating candidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating candidate", e);
        }
    }
    
    // Update candidate image
    public Candidate updateCandidateImage(String id, MultipartFile image) throws IOException, InterruptedException {
        logger.info("Updating image for candidate with ID: " + id);
        
        if (image == null || image.isEmpty()) {
            logger.warning("No image provided for update");
            throw new IllegalArgumentException("No image provided for update");
        }
        
        try {
            // Convert image to base64
            String imageData = Base64.getEncoder().encodeToString(image.getBytes());
            logger.info("Converted image to base64 (length: " + imageData.length() + ")");
            
            // Update just the image data in Firebase
            Map<String, Object> updates = new HashMap<>();
            updates.put("candidateImage", imageData);
            
            DatabaseReference candidateRef = firebaseDatabase.getReference("Candidates").child(id);
            candidateRef.updateChildrenAsync(updates);
            logger.info("Firebase update initiated for candidate: " + id);
            
            // Also fetch the existing candidate to return updated data
            Candidate existingCandidate;
            try {
                existingCandidate = getCandidateById(id);
                if (existingCandidate == null) {
                    throw new RuntimeException("Candidate not found with ID: " + id);
                }
                
                // Update the returned object with new image
                existingCandidate.setCandidateImage(imageData);
                logger.info("Image updated successfully for candidate: " + id);
                
                return existingCandidate;
            } catch (Exception e) {
                logger.severe("Error fetching candidate after image update: " + e.getMessage());
                
                // Create a minimal response with just the image
                Candidate response = new Candidate();
                response.setId(id);
                response.setCandidateImage(imageData);
                
                return response;
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.severe("Unexpected error updating image: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating candidate image", e);
        }
    }

    // Delete candidate
    public boolean deleteCandidate(String id) throws InterruptedException {
        logger.info("Deleting candidate with ID: " + id);
        try {
            DatabaseReference candidateRef = firebaseDatabase.getReference("Candidates").child(id);
            
            // Check if candidate exists before deletion
            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] exists = {false};
            
            candidateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    exists[0] = snapshot.exists();
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    latch.countDown();
                }
            });
            
            latch.await(5, TimeUnit.SECONDS);
            
            if (!exists[0]) {
                logger.warning("Candidate with ID " + id + " not found for deletion");
                return false;
            }
            
            // Delete the candidate
            candidateRef.removeValueAsync();
            logger.info("Candidate deleted successfully");
            return true;
            
        } catch (Exception e) {
            logger.severe("Error deleting candidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error deleting candidate", e);
        }
    }
}