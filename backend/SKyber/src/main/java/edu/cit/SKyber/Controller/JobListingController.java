package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.JobListing;
import edu.cit.SKyber.Service.JobListingService;
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
@RequestMapping("/api/jobs")
public class JobListingController {
    private static final Logger logger = Logger.getLogger(JobListingController.class.getName());
    
    @Autowired
    private JobListingService jobListingService;
    
    // Create job listing
    @PostMapping("/createJob")
    public ResponseEntity<JobListing> createJobListing(@RequestBody JobListing jobListing) {
        try {
            logger.info("Request received to create job listing");
            JobListing createdJobListing = jobListingService.createJobListing(jobListing);
            return new ResponseEntity<>(createdJobListing, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating job listing: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create job listing with image
    @PostMapping("/createJob/with-image")
    public ResponseEntity<JobListing> createJobListingWithImage(
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("companyName") String companyName,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("applicationlink") String applicationlink,
            @RequestParam("employementType") String employementType,
            @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to create job listing with image");
            
            JobListing jobListing = new JobListing();
            jobListing.setJobTitle(jobTitle);
            jobListing.setCompanyName(companyName);
            jobListing.setAddress(address);
            jobListing.setDescription(description);
            jobListing.setApplicationlink(applicationlink);
            jobListing.setEmployementType(employementType);
            
            // Process image if provided
            if (image != null && !image.isEmpty()) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                jobListing.setJobImage(base64Image);
            }
            
            JobListing createdJobListing = jobListingService.createJobListing(jobListing);
            return new ResponseEntity<>(createdJobListing, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating job listing with image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all job listings
    @GetMapping("/getAllJobs")
    public ResponseEntity<List<JobListing>> getAllJobListings() {
        try {
            logger.info("Request received to get all job listings");
            List<JobListing> jobListings = jobListingService.getAllJobListings();
            return new ResponseEntity<>(jobListings, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error retrieving job listings: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get job listing by ID
    @GetMapping("/getJob/{id}")
    public ResponseEntity<JobListing> getJobListingById(@PathVariable("id") String id) {
        try {
            logger.info("Request received to get job listing with ID: " + id);
            JobListing jobListing = jobListingService.getJobListingById(id);
            
            if (jobListing != null) {
                return new ResponseEntity<>(jobListing, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error retrieving job listing with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update job listing
    @PutMapping("/updateJob/{id}")
    public ResponseEntity<JobListing> updateJobListing(@PathVariable("id") String id, @RequestBody JobListing jobListing) {
        try {
            logger.info("Request received to update job listing with ID: " + id);
            JobListing updatedJobListing = jobListingService.updateJobListing(id, jobListing);
            
            if (updatedJobListing != null) {
                return new ResponseEntity<>(updatedJobListing, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error updating job listing with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update job listing image
    @PutMapping("/updateJob/{id}/image")
    public ResponseEntity<JobListing> updateJobImage(@PathVariable("id") String id, @RequestParam("image") MultipartFile image) {
        try {
            logger.info("Request received to update image for job listing with ID: " + id);
            JobListing updatedJobListing = jobListingService.updateJobImage(id, image);
            
            if (updatedJobListing != null) {
                return new ResponseEntity<>(updatedJobListing, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Error updating job listing image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.severe("Error updating job listing image with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete job listing
    @DeleteMapping("/deleteJob/{id}")
    public ResponseEntity<HttpStatus> deleteJobListing(@PathVariable("id") String id) {
        try {
            logger.info("Request received to delete job listing with ID: " + id);
            boolean deleted = jobListingService.deleteJobListing(id);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error deleting job listing with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Job listing controller is working");
    }
}