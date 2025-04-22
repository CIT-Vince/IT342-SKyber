package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Announcement;
import edu.cit.SKyber.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
=======
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {
    
    private static final Logger logger = Logger.getLogger(AnnouncementController.class.getName());
    
    @Autowired
<<<<<<< HEAD
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
=======
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // Fetch all announcements
    @GetMapping
    public List<Announcement> getAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    // Fetch a single announcement by ID
    @GetMapping("/{id}")
    public Announcement getAnnouncementById(@PathVariable("id") Long id) {
        return announcementService.getAnnouncementById(id);
    }

    // Create a new announcement
    @PostMapping
    public Announcement createAnnouncement(
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
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
<<<<<<< HEAD
    
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
    @GetMapping("/getAnnouncement/{id}")
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
=======

    // Update an existing announcement
    @PutMapping("/{id}")
    public Announcement updateAnnouncement(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("barangay") String barangay,
            @RequestParam("category") String category,
            @RequestParam("images") MultipartFile[] images) throws IOException {

        Announcement announcement = announcementService.getAnnouncementById(id);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setBarangay(barangay);
        announcement.setCategory(category);
        announcement.setPostedAt(LocalDateTime.now());

        return announcementService.saveAnnouncement(announcement);
    }

    // Delete an announcement by ID
    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
>>>>>>> 2a39962afdc48ed0d92a81f2f2e6f603c052b843
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
}