package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.ProjectTransparency;
import edu.cit.SKyber.Service.PTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/projects")
public class PTController {

    private static final Logger logger = Logger.getLogger(PTController.class.getName());

    @Autowired
    private PTService ptService;

    // Get all projects - UPDATED to match '/all' endpoint
    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        try {
            List<ProjectTransparency> projects = ptService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            logger.severe("Error retrieving projects: " + e.getMessage());
            e.printStackTrace(); // Add this for better debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving projects: " + e.getMessage());
        }
    }

    // Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        try {
            ProjectTransparency project = ptService.getProjectById(id);
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project not found with ID: " + id);
            }
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            logger.severe("Error retrieving project: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving project: " + e.getMessage());
        }
    }

    // Create project
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectTransparency project) {
        try {
            ProjectTransparency createdProject = ptService.createProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (Exception e) {
            logger.severe("Error creating project: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating project: " + e.getMessage());
        }
    }

    // Create project with image
    @PostMapping("/createWithImage")
    public ResponseEntity<?> createProjectWithImage(
            @RequestParam("projectName") String projectName,
            @RequestParam("description") String description,
            @RequestParam("budget") Double budget,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("status") String status,
            @RequestParam("projectManager") String projectManager,
            @RequestParam(value = "teamMembers", required = false) String teamMembers,
            @RequestParam(value = "stakeholders", required = false) String stakeholders,
            @RequestParam(value = "sustainabilityGoals", required = false) String sustainabilityGoals,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            ProjectTransparency createdProject = ptService.createProjectWithImage(
                    projectName, description, budget, startDate, endDate, status,
                    projectManager, teamMembers, stakeholders, sustainabilityGoals, image);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error creating project: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating project: " + e.getMessage());
        }
    }

    // Update project
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable String id,
            @RequestBody ProjectTransparency project) {
        try {
            ProjectTransparency updatedProject = ptService.updateProject(id, project);
            return ResponseEntity.ok(updatedProject);
        } catch (Exception e) {
            logger.severe("Error updating project: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating project: " + e.getMessage());
        }
    }

    // Update project image - improved version
    @PostMapping("/{id}/updateImage")
    public ResponseEntity<?> updateProjectImage(
            @PathVariable String id,
            @RequestParam("image") MultipartFile image) {
        
        logger.info("Image update request for project: " + id);
        
        try {
            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest().body("No image provided");
            }
            
            // Log image details to help debug
            logger.info("Image details - Name: " + image.getOriginalFilename() + 
                        ", Size: " + image.getSize() + 
                        ", Content Type: " + image.getContentType());
            
            ProjectTransparency updatedProject = ptService.updateProjectImage(id, image);
            
            if (updatedProject == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to update image - null response from service");
            }
            
            logger.info("Image updated successfully for project: " + id);
            return ResponseEntity.ok(updatedProject);
            
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error updating project image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating project image: " + e.getMessage());
        }
    }

    // Delete project
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        try {
            boolean deleted = ptService.deleteProject(id);
            if (deleted) {
                Map<String, Boolean> response = new HashMap<>();
                response.put("deleted", true);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project not found with ID: " + id);
            }
        } catch (Exception e) {
            logger.severe("Error deleting project: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting project: " + e.getMessage());
        }
    }
}