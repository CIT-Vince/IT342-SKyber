package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Volunteer;
import edu.cit.SKyber.Service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {
    private static final Logger logger = Logger.getLogger(VolunteerController.class.getName());
    
    @Autowired
    private VolunteerService volunteerService;
    
    // Create volunteer opportunity
    @PostMapping("/createVolunteer")
    public ResponseEntity<Volunteer> createVolunteer(@RequestBody Volunteer volunteer) {
        try {
            logger.info("Request received to create volunteer opportunity");
            Volunteer createdVolunteer = volunteerService.createVolunteer(volunteer);
            return new ResponseEntity<>(createdVolunteer, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating volunteer opportunity: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create volunteer opportunity with image
    @PostMapping("/createVolunteer/with-image")
    public ResponseEntity<Volunteer> createVolunteerWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "registerLink", required = false) String registerLink,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "eventDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
            @RequestParam(value = "contactPerson", required = false) String contactPerson,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "requirements", required = false) String requirements,
            @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to create volunteer opportunity with image");
            
            Volunteer volunteer = new Volunteer();
            volunteer.setTitle(title);
            volunteer.setDescription(description);
            volunteer.setRegisterLink(registerLink);
            volunteer.setCategory(category);
            volunteer.setLocation(location);
            volunteer.setEventDate(eventDate);
            volunteer.setContactPerson(contactPerson);
            volunteer.setContactEmail(contactEmail);
            volunteer.setStatus(status);
            volunteer.setRequirements(requirements);
            
            // Process image if provided
            if (image != null && !image.isEmpty()) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                volunteer.setVolunteerImage(base64Image);
            }
            
            Volunteer createdVolunteer = volunteerService.createVolunteer(volunteer);
            return new ResponseEntity<>(createdVolunteer, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating volunteer opportunity with image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all volunteer opportunities
    @GetMapping("/getAllVolunteers")
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        try {
            logger.info("Request received to get all volunteer opportunities");
            List<Volunteer> volunteers = volunteerService.getAllVolunteers();
            return new ResponseEntity<>(volunteers, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving volunteer opportunities: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get volunteer opportunity by ID
    @GetMapping("/getVolunteer/{id}")
    public ResponseEntity<Volunteer> getVolunteerById(@PathVariable("id") String id) {
        try {
            logger.info("Request received to get volunteer opportunity with ID: " + id);
            Volunteer volunteer = volunteerService.getVolunteerById(id);
            
            if (volunteer != null) {
                return new ResponseEntity<>(volunteer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error retrieving volunteer opportunity with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update volunteer opportunity
    @PutMapping("/updateVolunteer/{id}")
    public ResponseEntity<Volunteer> updateVolunteer(@PathVariable("id") String id, @RequestBody Volunteer volunteer) {
        try {
            logger.info("Request received to update volunteer opportunity with ID: " + id);
            Volunteer updatedVolunteer = volunteerService.updateVolunteer(id, volunteer);
            
            if (updatedVolunteer != null) {
                return new ResponseEntity<>(updatedVolunteer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error updating volunteer opportunity with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update volunteer image
    @PutMapping("/updateVolunteer/{id}/image")
    public ResponseEntity<Volunteer> updateVolunteerImage(@PathVariable("id") String id, @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to update image for volunteer opportunity with ID: " + id);
            Volunteer updatedVolunteer = volunteerService.updateVolunteerImage(id, image);
            
            if (updatedVolunteer != null) {
                return new ResponseEntity<>(updatedVolunteer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Error updating volunteer image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.severe("Error updating volunteer image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete volunteer opportunity
    @DeleteMapping("/deleteVolunteer/{id}")
    public ResponseEntity<HttpStatus> deleteVolunteer(@PathVariable("id") String id) {
        try {
            logger.info("Request received to delete volunteer opportunity with ID: " + id);
            boolean deleted = volunteerService.deleteVolunteer(id);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error deleting volunteer opportunity with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Volunteer controller is working");
    }
}