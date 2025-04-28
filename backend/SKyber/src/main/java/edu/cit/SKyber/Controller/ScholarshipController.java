package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Scholarship;
import edu.cit.SKyber.Service.ScholarshipService;
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
@RequestMapping("/api/scholarships")
public class ScholarshipController {
    private static final Logger logger = Logger.getLogger(ScholarshipController.class.getName());
    
    @Autowired
    private ScholarshipService scholarshipService;
    
    // Create scholarship
    @PostMapping("/createScholarship")
    public ResponseEntity<Scholarship> createScholarship(@RequestBody Scholarship scholarship) {
        try {
            logger.info("Request received to create scholarship");
            Scholarship createdScholarship = scholarshipService.createScholarship(scholarship);
            return new ResponseEntity<>(createdScholarship, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating scholarship: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create scholarship with image
    @PostMapping("/createScholarship/with-image")
    public ResponseEntity<Scholarship> createScholarshipWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("link") String link,
            @RequestParam("contactEmail") String contactEmail,
            @RequestParam("type") String type,
            @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to create scholarship with image");
            
            Scholarship scholarship = new Scholarship();
            scholarship.setTitle(title);
            scholarship.setDescription(description);
            scholarship.setLink(link);
            scholarship.setContactEmail(contactEmail);
            scholarship.setType(type);
            
            // Process image if provided
            if (image != null && !image.isEmpty()) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                scholarship.setScholarImage(base64Image);
            }
            
            Scholarship createdScholarship = scholarshipService.createScholarship(scholarship);
            return new ResponseEntity<>(createdScholarship, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating scholarship with image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all scholarships
    @GetMapping("/getAllScholarships")
    public ResponseEntity<List<Scholarship>> getAllScholarships() {
        try {
            logger.info("Request received to get all scholarships");
            List<Scholarship> scholarships = scholarshipService.getAllScholarships();
            return new ResponseEntity<>(scholarships, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving scholarships: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get scholarship by ID
    @GetMapping("/getScholarship/{id}")
    public ResponseEntity<Scholarship> getScholarshipById(@PathVariable("id") String id) {
        try {
            logger.info("Request received to get scholarship with ID: " + id);
            Scholarship scholarship = scholarshipService.getScholarshipById(id);
            
            if (scholarship != null) {
                return new ResponseEntity<>(scholarship, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error retrieving scholarship with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update scholarship
    @PutMapping("/updateScholarship/{id}")
    public ResponseEntity<Scholarship> updateScholarship(@PathVariable("id") String id, @RequestBody Scholarship scholarship) {
        try {
            logger.info("Request received to update scholarship with ID: " + id);
            Scholarship updatedScholarship = scholarshipService.updateScholarship(id, scholarship);
            
            if (updatedScholarship != null) {
                return new ResponseEntity<>(updatedScholarship, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error updating scholarship with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update scholarship image
    @PutMapping("/updateScholarship/{id}/image")
    public ResponseEntity<Scholarship> updateScholarshipImage(@PathVariable("id") String id, @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to update image for scholarship with ID: " + id);
            Scholarship updatedScholarship = scholarshipService.updateScholarshipImage(id, image);
            
            if (updatedScholarship != null) {
                return new ResponseEntity<>(updatedScholarship, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Error updating scholarship image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.severe("Error updating scholarship image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete scholarship
    @DeleteMapping("/deleteScholarship/{id}")
    public ResponseEntity<HttpStatus> deleteScholarship(@PathVariable("id") String id) {
        try {
            logger.info("Request received to delete scholarship with ID: " + id);
            boolean deleted = scholarshipService.deleteScholarship(id);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error deleting scholarship with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Scholarship controller is working");
    }
}