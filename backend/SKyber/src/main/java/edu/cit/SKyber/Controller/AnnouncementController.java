package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Announcement;
import edu.cit.SKyber.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    
    private static final Logger logger = Logger.getLogger(AnnouncementController.class.getName());
    private Map<String, String> idToOriginalIdMap = new HashMap<>();
    
    @Autowired
    private AnnouncementService announcementService;
    
    // Create a new announcement
    @PostMapping("/createAnnouncements")
    public ResponseEntity<?> createAnnouncement(@RequestBody Announcement announcement) {
        try {
            Announcement newAnnouncement = announcementService.createAnnouncement(announcement);
            return new ResponseEntity<>(newAnnouncement, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating announcement: " + e.getMessage());
            return new ResponseEntity<>("Error creating announcement: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create announcement with image upload
    @PostMapping(value = "/createWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAnnouncementWithImage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("barangay") String barangay,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image) {
        
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setBarangay(barangay);
            announcement.setCategory(category);
            
            // Convert image to Base64
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            announcement.setImageData(base64Image);
            
            Announcement newAnnouncement = announcementService.createAnnouncement(announcement);
            return new ResponseEntity<>(newAnnouncement, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>("Error processing image: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.severe("Error creating announcement with image: " + e.getMessage());
            return new ResponseEntity<>("Error creating announcement with image: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all announcements
    @GetMapping("/getAllAnnouncements")
    public ResponseEntity<?> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.getAllAnnouncements();
            return new ResponseEntity<>(announcements, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving announcements: " + e.getMessage());
            return new ResponseEntity<>("Error retrieving announcements: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get announcement by ID
    @GetMapping("/getAnnouncements/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable String id) {
        try {
            Announcement announcement = announcementService.getAnnouncementById(id);
            if (announcement != null) {
                return new ResponseEntity<>(announcement, HttpStatus.OK);
            }
            return new ResponseEntity<>("Announcement not found with id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.severe("Error retrieving announcement: " + e.getMessage());
            return new ResponseEntity<>("Error retrieving announcement: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update an announcement
    @PutMapping("/updateAnnouncement/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable String id, @RequestBody Announcement announcement) {
        try {
            Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcement);
            if (updatedAnnouncement != null) {
                return new ResponseEntity<>(updatedAnnouncement, HttpStatus.OK);
            }
            return new ResponseEntity<>("Announcement not found with id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.severe("Error updating announcement: " + e.getMessage());
            return new ResponseEntity<>("Error updating announcement: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete an announcement
    @DeleteMapping("/deleteAnnouncement/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable String id) {
        try {
            boolean deleted = announcementService.deleteAnnouncement(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("Announcement not found with id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.severe("Error deleting announcement: " + e.getMessage());
            return new ResponseEntity<>("Error deleting announcement: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // In AnnouncementController.java - add a new endpoint

@GetMapping("/getAnnouncementByHashId/{hashId}")
public ResponseEntity<?> getAnnouncementByHashId(@PathVariable String hashId) {
    try {
        // Convert hashId to Long for comparison
        Long numericId = Long.valueOf(hashId);
        
        // Get all announcements
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        
        // Find the one with matching hash ID
        for (Announcement ann : announcements) {
            if (ann.getId().equals(numericId)) {
                return ResponseEntity.ok(ann);
            }
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No announcement found with hash ID: " + hashId);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error retrieving announcement: " + e.getMessage());
    }
}
}