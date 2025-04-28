package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Candidate;
import edu.cit.SKyber.Service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    
    private static final Logger logger = Logger.getLogger(CandidateController.class.getName());
    
    @Autowired
    private CandidateService candidateService;
    
    // Get all candidates
    @GetMapping("/getAllCandidates") 
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        try {
            logger.info("Request received to get all candidates");
            List<Candidate> candidates = candidateService.getAllCandidates();
            return new ResponseEntity<>(candidates, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving all candidates: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get candidate by ID
    @GetMapping("/getCandidate/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable("id") String id) {
        try {
            logger.info("Request received to get candidate with ID: " + id);
            Candidate candidate = candidateService.getCandidateById(id);
            
            if (candidate != null) {
                return new ResponseEntity<>(candidate, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error retrieving candidate with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create candidate without image
    @PostMapping("/createCandidate")
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        try {
            logger.info("Request received to create candidate without image");
            Candidate createdCandidate = candidateService.createCandidate(candidate);
            return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating candidate: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create candidate with image
    @PostMapping("/createCandidate/with-image")
    public ResponseEntity<?> createCandidateWithImage(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "age", required = false) String age,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "partylist", required = false) String partylist,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            logger.info("Request received to create candidate with image: " + firstName + " " + lastName);
            Candidate createdCandidate = candidateService.createCandidateWithImage(firstName, lastName, age, 
                    address, partylist, platform, image);
            return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error creating candidate: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating candidate: " + e.getMessage());
        }
    }
    
    // Update candidate
    @PutMapping("/updateCandidate/{id}")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable("id") String id, 
            @RequestBody Candidate candidateDetails) {
        try {
            logger.info("Request received to update candidate with ID: " + id);
            Candidate updatedCandidate = candidateService.updateCandidate(id, candidateDetails);
            return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error updating candidate with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update candidate image
    @PutMapping("/updateCandidate/{id}/image")
    public ResponseEntity<Candidate> updateCandidateImage(
            @PathVariable("id") String id,
            @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to update image for candidate with ID: " + id);
            Candidate updatedCandidate = candidateService.updateCandidateImage(id, image);
            return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
        } catch (InterruptedException e) {
            logger.severe("Thread interrupted while updating candidate image: " + e.getMessage());
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.severe("Error updating candidate image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete candidate
    @DeleteMapping("/deleteCandidate/{id}")
    public ResponseEntity<HttpStatus> deleteCandidate(@PathVariable("id") String id) {
        try {
            logger.info("Request received to delete candidate with ID: " + id);
            boolean deleted = candidateService.deleteCandidate(id);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error deleting candidate with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller working");
    }
}