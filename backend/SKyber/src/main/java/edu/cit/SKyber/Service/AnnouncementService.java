package edu.cit.SKyber.Service;

import com.google.firebase.database.*;
import edu.cit.SKyber.Entity.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class AnnouncementService {

    private static final Logger logger = Logger.getLogger(AnnouncementService.class.getName());
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create a new announcement
    public Announcement createAnnouncement(Announcement announcement) {
        try {
            logger.info("Creating new announcement with title: " + announcement.getTitle());
            announcement.setPostedAt(LocalDateTime.now());
            
            // Create a map for Firebase compatibility
            Map<String, Object> announcementMap = new HashMap<>();
            announcementMap.put("title", announcement.getTitle());
            announcementMap.put("content", announcement.getContent());
            announcementMap.put("barangay", announcement.getBarangay());
            announcementMap.put("category", announcement.getCategory());
            announcementMap.put("postedAtString", announcement.getPostedAt().toString());
            
            // Add imageData only if present
            if (announcement.getImageData() != null && !announcement.getImageData().isEmpty()) {
                announcementMap.put("imageData", announcement.getImageData());
            }
            
            // Push to Firebase and get key
            DatabaseReference announcementRef = firebaseDatabase.getReference("Announcements").push();
            String key = announcementRef.getKey();
            
            // Store the key as id in the map and object
            announcementMap.put("id", key); 
            announcement.setId(key); // Use hashCode for numeric ID
            
            // Save to Firebase
            announcementRef.setValueAsync(announcementMap);
            
            logger.info("Announcement created with ID: " + key);
            return announcement;
            
        } catch (Exception e) {
            logger.severe("Error creating announcement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating announcement", e);
        }
    }

    // Get all announcements
    public List<Announcement> getAllAnnouncements() throws InterruptedException {
        logger.info("Fetching all announcements");
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Announcement> announcements = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();
        logger.info("Firebase database URL: " + firebaseDatabase.getReference().toString());
        DatabaseReference connRef = firebaseDatabase.getReference(".info/connected");
        connRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                logger.info("Firebase connection status: " + (connected ? "CONNECTED" : "DISCONNECTED"));
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                logger.severe("Firebase connection check failed: " + error.getMessage());
            }
        });
        try {
            DatabaseReference announcementsRef = firebaseDatabase.getReference("Announcements");
            Query limitedQuery = announcementsRef.limitToFirst(10);
            limitedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        logger.info("Retrieved " + snapshot.getChildrenCount() + " announcements from Firebase");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                
                                // Manual mapping for more reliable deserialization
                                Announcement announcement = new Announcement();
                                announcement.setId(key); // Use hashCode for numeric ID
                                
                                if (dataSnapshot.hasChild("title")) {
                                    announcement.setTitle(dataSnapshot.child("title").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("content")) {
                                    announcement.setContent(dataSnapshot.child("content").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("barangay")) {
                                    announcement.setBarangay(dataSnapshot.child("barangay").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("category")) {
                                    announcement.setCategory(dataSnapshot.child("category").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("imageData")) {
                                    announcement.setImageData(dataSnapshot.child("imageData").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("postedAtString")) {
                                    String dateStr = dataSnapshot.child("postedAtString").getValue(String.class);
                                    try {
                                        announcement.setPostedAt(LocalDateTime.parse(dateStr));
                                    } catch (Exception e) {
                                        logger.warning("Error parsing date: " + dateStr + ". Using current time.");
                                        announcement.setPostedAt(LocalDateTime.now());
                                    }
                                } else {
                                    announcement.setPostedAt(LocalDateTime.now());
                                }
                                
                                announcements.add(announcement);
                                logger.fine("Processed announcement: " + announcement.getTitle());
                            } catch (Exception e) {
                                logger.warning("Error processing announcement: " + e.getMessage());
                                // Continue to next announcement
                            }
                        }
                    } catch (Exception e) {
                        logger.severe("Error in data change processing: " + e.getMessage());
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
                logger.warning("Firebase query took longer than 10 seconds - returning partial results");
                // Return whatever we have instead of throwing an exception
                return announcements;
            }
            
            if (!exceptions.isEmpty()) {
                logger.warning("Encountered errors but returning partial results: " + 
                              exceptions.get(0).getMessage());
                // Return partial results even if there were some errors
                return announcements;
            }
            
            logger.info("Successfully retrieved " + announcements.size() + " announcements");
            return announcements;
            
        } catch (Exception e) {
            logger.severe("Error getting announcements: " + e.getMessage());
            e.printStackTrace();
            // Return empty list instead of throwing exception
            return new ArrayList<>();
        }
    }

    // Get announcements by barangay
    public List<Announcement> getAnnouncementsByBarangay(String barangay) throws InterruptedException {
        logger.info("Fetching announcements for barangay: " + barangay);
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Announcement> announcements = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference announcementsRef = firebaseDatabase.getReference("Announcements");
            Query query = announcementsRef.orderByChild("barangay").equalTo(barangay);
            
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                
                                // Manual mapping as in getAllAnnouncements
                                Announcement announcement = new Announcement();
                                announcement.setId(key);
                                
                                if (dataSnapshot.hasChild("title")) {
                                    announcement.setTitle(dataSnapshot.child("title").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("content")) {
                                    announcement.setContent(dataSnapshot.child("content").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("barangay")) {
                                    announcement.setBarangay(dataSnapshot.child("barangay").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("category")) {
                                    announcement.setCategory(dataSnapshot.child("category").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("imageData")) {
                                    announcement.setImageData(dataSnapshot.child("imageData").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("postedAtString")) {
                                    String dateStr = dataSnapshot.child("postedAtString").getValue(String.class);
                                    try {
                                        announcement.setPostedAt(LocalDateTime.parse(dateStr));
                                    } catch (Exception e) {
                                        announcement.setPostedAt(LocalDateTime.now());
                                    }
                                } else {
                                    announcement.setPostedAt(LocalDateTime.now());
                                }
                                
                                announcements.add(announcement);
                            } catch (Exception e) {
                                logger.warning("Error processing announcement: " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
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
            
            logger.info("Retrieved " + announcements.size() + " announcements for barangay: " + barangay);
            return announcements;
            
        } catch (Exception e) {
            logger.severe("Error getting announcements by barangay: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching announcements by barangay", e);
        }
    }

    // Get announcements by category
    public List<Announcement> getAnnouncementsByCategory(String category) throws InterruptedException {
        logger.info("Fetching announcements for category: " + category);
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Announcement> announcements = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference announcementsRef = firebaseDatabase.getReference("Announcements");
            Query query = announcementsRef.orderByChild("category").equalTo(category);
            
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                
                                // Manual mapping as in other methods
                                Announcement announcement = new Announcement();
                                announcement.setId(key);
                                
                                // Same mapping as previous methods...
                                if (dataSnapshot.hasChild("title")) {
                                    announcement.setTitle(dataSnapshot.child("title").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("content")) {
                                    announcement.setContent(dataSnapshot.child("content").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("barangay")) {
                                    announcement.setBarangay(dataSnapshot.child("barangay").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("category")) {
                                    announcement.setCategory(dataSnapshot.child("category").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("imageData")) {
                                    announcement.setImageData(dataSnapshot.child("imageData").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("postedAtString")) {
                                    String dateStr = dataSnapshot.child("postedAtString").getValue(String.class);
                                    try {
                                        announcement.setPostedAt(LocalDateTime.parse(dateStr));
                                    } catch (Exception e) {
                                        announcement.setPostedAt(LocalDateTime.now());
                                    }
                                } else {
                                    announcement.setPostedAt(LocalDateTime.now());
                                }
                                
                                announcements.add(announcement);
                            } catch (Exception e) {
                                logger.warning("Error processing announcement: " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
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
            
            return announcements;
            
        } catch (Exception e) {
            logger.severe("Error getting announcements by category: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching announcements by category", e);
        }
    }

    // Get announcement by id
    public Announcement getAnnouncementById(String id) throws InterruptedException {
        logger.info("Fetching announcement with ID: " + id);
        final CountDownLatch latch = new CountDownLatch(1);
        final Announcement[] announcement = new Announcement[1];
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference announcementRef = firebaseDatabase.getReference("Announcements").child(id);
            
            announcementRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            logger.warning("No announcement found with ID: " + id);
                            latch.countDown();
                            return;
                        }
                        
                        String key = snapshot.getKey();
                        
                        // Manual mapping as in other methods
                        Announcement ann = new Announcement();
                        ann.setId(key);
                        
                        if (snapshot.hasChild("title")) {
                            ann.setTitle(snapshot.child("title").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("content")) {
                            ann.setContent(snapshot.child("content").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("barangay")) {
                            ann.setBarangay(snapshot.child("barangay").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("category")) {
                            ann.setCategory(snapshot.child("category").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("imageData")) {
                            ann.setImageData(snapshot.child("imageData").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("postedAtString")) {
                            String dateStr = snapshot.child("postedAtString").getValue(String.class);
                            try {
                                ann.setPostedAt(LocalDateTime.parse(dateStr));
                            } catch (Exception e) {
                                ann.setPostedAt(LocalDateTime.now());
                            }
                        } else {
                            ann.setPostedAt(LocalDateTime.now());
                        }
                        
                        announcement[0] = ann;
                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
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
            
            return announcement[0];
            
        } catch (Exception e) {
            logger.severe("Error getting announcement by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching announcement by ID", e);
        }
    }

    // Update announcement
    public Announcement updateAnnouncement(String id, Announcement announcementDetails) {
        logger.info("Updating announcement with ID: " + id);
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> announcementMap = new HashMap<>();
            announcementMap.put("id", id);
            announcementMap.put("title", announcementDetails.getTitle());
            announcementMap.put("content", announcementDetails.getContent());
            announcementMap.put("barangay", announcementDetails.getBarangay());
            announcementMap.put("category", announcementDetails.getCategory());
            
            // Only update posted time if not present
            if (announcementDetails.getPostedAt() == null) {
                announcementDetails.setPostedAt(LocalDateTime.now());
            }
            announcementMap.put("postedAtString", announcementDetails.getPostedAt().toString());
            
            // Handle image data
            if (announcementDetails.getImageData() != null && !announcementDetails.getImageData().isEmpty()) {
                announcementMap.put("imageData", announcementDetails.getImageData());
            }
            
            // Update in Firebase
            DatabaseReference announcementRef = firebaseDatabase.getReference("Announcements").child(id);
            announcementRef.updateChildrenAsync(announcementMap);
            
            // Set the ID in the returned object for consistency
            announcementDetails.setId(id);
            
            logger.info("Announcement updated successfully");
            return announcementDetails;
            
        } catch (Exception e) {
            logger.severe("Error updating announcement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating announcement", e);
        }
    }

    // Delete announcement
    public boolean deleteAnnouncement(String id) {
        logger.info("Deleting announcement with ID: " + id);
        try {
            DatabaseReference announcementRef = firebaseDatabase.getReference("Announcements").child(id);
            
            // Check if the announcement exists before deletion
            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] exists = {false};
            
            announcementRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                logger.warning("Announcement with ID " + id + " not found for deletion");
                return false;
            }
            
            // Delete the announcement
            announcementRef.removeValueAsync();
            logger.info("Announcement deleted successfully");
            return true;
            
        } catch (Exception e) {
            logger.severe("Error deleting announcement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error deleting announcement", e);
        }
    }
}