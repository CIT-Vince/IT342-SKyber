package edu.cit.SKyber.Service;

import com.google.firebase.database.*;
import edu.cit.SKyber.Entity.ProjectTransparency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Base64;

@Service
public class PTService {
    
    private static final Logger logger = Logger.getLogger(PTService.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Create a new project
    public ProjectTransparency createProject(ProjectTransparency project) {
        try {
            logger.info("Creating new project with name: " + project.getProjectName());
            
            // Create a map for Firebase compatibility
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("projectName", project.getProjectName());
            projectMap.put("description", project.getDescription());
            projectMap.put("budget", project.getBudget());
            
            // Convert LocalDate to String for Firebase storage
            if (project.getStartDate() != null) {
                projectMap.put("startDate", project.getStartDate().format(DATE_FORMATTER));
            }
            
            if (project.getEndDate() != null) {
                projectMap.put("endDate", project.getEndDate().format(DATE_FORMATTER));
            }
            
            projectMap.put("status", project.getStatus());
            projectMap.put("projectManager", project.getProjectManager());
            projectMap.put("teamMembers", project.getTeamMembers());
            projectMap.put("stakeholders", project.getStakeholders());
            projectMap.put("sustainabilityGoals", project.getSustainabilityGoals());
            
            // Store image if available
            if (project.getProjectImage() != null && !project.getProjectImage().isEmpty()) {
                projectMap.put("projectImage", project.getProjectImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference projectRef = firebaseDatabase.getReference("projects").push();
            String key = projectRef.getKey();
            
            // Store the key as id in the map and object
            projectMap.put("id", key);
            project.setId(Long.valueOf(key.hashCode())); // Use hashCode for numeric ID
            
            // Save to Firebase
            projectRef.setValueAsync(projectMap);
            
            logger.info("Project created with ID: " + key);
            return project;
            
        } catch (Exception e) {
            logger.severe("Error creating project: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating project", e);
        }
    }

    // Create project with image
    public ProjectTransparency createProjectWithImage(
            String projectName, 
            String description, 
            Double budget, 
            LocalDate startDate,
            LocalDate endDate,
            String status, 
            String projectManager,
            String teamMembers,
            String stakeholders,
            String sustainabilityGoals,
            MultipartFile image) throws IOException {
        
        logger.info("Creating project with image: " + projectName);
        ProjectTransparency project = new ProjectTransparency();
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setBudget(budget);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setStatus(status);
        project.setProjectManager(projectManager);
        project.setTeamMembers(teamMembers);
        project.setStakeholders(stakeholders);
        project.setSustainabilityGoals(sustainabilityGoals);
        
        if (image != null && !image.isEmpty()) {
            // Convert MultipartFile to Base64 encoded string
            project.setProjectImage(Base64.getEncoder().encodeToString(image.getBytes()));
            logger.info("Image attached to project");
        }
        
        return createProject(project);
    }

    // Get all projects
    public List<ProjectTransparency> getAllProjects() throws InterruptedException {
        logger.info("Fetching all projects");
        final CountDownLatch latch = new CountDownLatch(1);
        final List<ProjectTransparency> projects = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference projectsRef = firebaseDatabase.getReference("projects");
            projectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        logger.info("Retrieved " + snapshot.getChildrenCount() + " projects from Firebase");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                
                                // Create a new project
                                ProjectTransparency project = new ProjectTransparency();
                                project.setId(Long.valueOf(key.hashCode())); // Use hashCode for numeric ID
                                
                                // Map fields from Firebase to entity
                                if (dataSnapshot.hasChild("projectName")) {
                                    project.setProjectName(dataSnapshot.child("projectName").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("description")) {
                                    project.setDescription(dataSnapshot.child("description").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("budget")) {
                                    project.setBudget(dataSnapshot.child("budget").getValue(Double.class));
                                }
                                
                                // Convert string dates back to LocalDate
                                if (dataSnapshot.hasChild("startDate")) {
                                    String dateStr = dataSnapshot.child("startDate").getValue(String.class);
                                    try {
                                        project.setStartDate(LocalDate.parse(dateStr, DATE_FORMATTER));
                                    } catch (Exception e) {
                                        logger.warning("Error parsing start date: " + dateStr);
                                    }
                                }
                                
                                if (dataSnapshot.hasChild("endDate")) {
                                    String dateStr = dataSnapshot.child("endDate").getValue(String.class);
                                    try {
                                        project.setEndDate(LocalDate.parse(dateStr, DATE_FORMATTER));
                                    } catch (Exception e) {
                                        logger.warning("Error parsing end date: " + dateStr);
                                    }
                                }
                                
                                if (dataSnapshot.hasChild("status")) {
                                    project.setStatus(dataSnapshot.child("status").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("projectManager")) {
                                    project.setProjectManager(dataSnapshot.child("projectManager").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("teamMembers")) {
                                    project.setTeamMembers(dataSnapshot.child("teamMembers").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("stakeholders")) {
                                    project.setStakeholders(dataSnapshot.child("stakeholders").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("sustainabilityGoals")) {
                                    project.setSustainabilityGoals(dataSnapshot.child("sustainabilityGoals").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("projectImage")) {
                                    project.setProjectImage(dataSnapshot.child("projectImage").getValue(String.class));
                                }
                                
                                projects.add(project);
                                logger.fine("Processed project: " + project.getProjectName());
                            } catch (Exception e) {
                                logger.warning("Error processing project: " + e.getMessage());
                                // Continue to next project
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
                throw new RuntimeException("Firebase query timed out after 10 seconds");
            }
            
            if (!exceptions.isEmpty()) {
                throw exceptions.get(0);
            }
            
            logger.info("Successfully retrieved " + projects.size() + " projects");
            return projects;
            
        } catch (Exception e) {
            logger.severe("Error getting projects: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching projects", e);
        }
    }

    // Get project by ID
    public ProjectTransparency getProjectById(String id) throws InterruptedException {
        logger.info("Fetching project with ID: " + id);
        final CountDownLatch latch = new CountDownLatch(1);
        final ProjectTransparency[] project = new ProjectTransparency[1];
        final List<Exception> exceptions = new ArrayList<>();

        try {
            DatabaseReference projectRef = firebaseDatabase.getReference("projects").child(id);
            
            projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            logger.warning("No project found with ID: " + id);
                            latch.countDown();
                            return;
                        }
                        
                        String key = snapshot.getKey();
                        
                        // Create a new project
                        ProjectTransparency p = new ProjectTransparency();
                        p.setId(Long.valueOf(key.hashCode()));
                        
                        // Map fields from Firebase
                        if (snapshot.hasChild("projectName")) {
                            p.setProjectName(snapshot.child("projectName").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("description")) {
                            p.setDescription(snapshot.child("description").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("budget")) {
                            p.setBudget(snapshot.child("budget").getValue(Double.class));
                        }
                        
                        // Convert string dates back to LocalDate
                        if (snapshot.hasChild("startDate")) {
                            String dateStr = snapshot.child("startDate").getValue(String.class);
                            try {
                                p.setStartDate(LocalDate.parse(dateStr, DATE_FORMATTER));
                            } catch (Exception e) {
                                logger.warning("Error parsing start date: " + dateStr);
                            }
                        }
                        
                        if (snapshot.hasChild("endDate")) {
                            String dateStr = snapshot.child("endDate").getValue(String.class);
                            try {
                                p.setEndDate(LocalDate.parse(dateStr, DATE_FORMATTER));
                            } catch (Exception e) {
                                logger.warning("Error parsing end date: " + dateStr);
                            }
                        }
                        
                        if (snapshot.hasChild("status")) {
                            p.setStatus(snapshot.child("status").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("projectManager")) {
                            p.setProjectManager(snapshot.child("projectManager").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("teamMembers")) {
                            p.setTeamMembers(snapshot.child("teamMembers").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("stakeholders")) {
                            p.setStakeholders(snapshot.child("stakeholders").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("sustainabilityGoals")) {
                            p.setSustainabilityGoals(snapshot.child("sustainabilityGoals").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("projectImage")) {
                            p.setProjectImage(snapshot.child("projectImage").getValue(String.class));
                        }
                        
                        project[0] = p;
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
            
            return project[0];
            
        } catch (Exception e) {
            logger.severe("Error getting project by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching project by ID", e);
        }
    }

    // Update project
    public ProjectTransparency updateProject(String id, ProjectTransparency projectDetails) {
        logger.info("Updating project with ID: " + id);
        try {
            // Create a map for Firebase compatibility
            Map<String, Object> updates = new HashMap<>();
            
            // Only update fields that are provided
            if (projectDetails.getProjectName() != null) {
                updates.put("projectName", projectDetails.getProjectName());
            }
            
            if (projectDetails.getDescription() != null) {
                updates.put("description", projectDetails.getDescription());
            }
            
            if (projectDetails.getBudget() != null) {
                updates.put("budget", projectDetails.getBudget());
            }
            
            if (projectDetails.getStartDate() != null) {
                updates.put("startDate", projectDetails.getStartDate().format(DATE_FORMATTER));
            }
            
            if (projectDetails.getEndDate() != null) {
                updates.put("endDate", projectDetails.getEndDate().format(DATE_FORMATTER));
            }
            
            if (projectDetails.getStatus() != null) {
                updates.put("status", projectDetails.getStatus());
            }
            
            if (projectDetails.getProjectManager() != null) {
                updates.put("projectManager", projectDetails.getProjectManager());
            }
            
            if (projectDetails.getTeamMembers() != null) {
                updates.put("teamMembers", projectDetails.getTeamMembers());
            }
            
            if (projectDetails.getStakeholders() != null) {
                updates.put("stakeholders", projectDetails.getStakeholders());
            }
            
            if (projectDetails.getSustainabilityGoals() != null) {
                updates.put("sustainabilityGoals", projectDetails.getSustainabilityGoals());
            }
            
            if (projectDetails.getProjectImage() != null && !projectDetails.getProjectImage().isEmpty()) {
                updates.put("projectImage", projectDetails.getProjectImage());
            }
            
            // Update in Firebase
            DatabaseReference projectRef = firebaseDatabase.getReference("projects").child(id);
            projectRef.updateChildrenAsync(updates);
            
            // Set the ID in the returned object for consistency
            projectDetails.setId(Long.valueOf(id.hashCode()));
            
            logger.info("Project updated successfully");
            return projectDetails;
            
        } catch (Exception e) {
            logger.severe("Error updating project: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating project", e);
        }
    }

    // Update project image
    public ProjectTransparency updateProjectImage(String id, MultipartFile image) throws IOException, InterruptedException {
        logger.info("Updating image for project with ID: " + id);
        
        // First get the existing project
        ProjectTransparency existingProject = getProjectById(id);
        
        if (existingProject == null) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        
        if (image != null && !image.isEmpty()) {
            // Convert image to base64
            String imageData = Base64.getEncoder().encodeToString(image.getBytes());
            
            // Update just the image data
            Map<String, Object> updates = new HashMap<>();
            updates.put("projectImage", imageData);
            
            DatabaseReference projectRef = firebaseDatabase.getReference("projects").child(id);
            projectRef.updateChildrenAsync(updates);
            
            // Update the returned object
            existingProject.setProjectImage(imageData);
        }
        
        return existingProject;
    }

    // Delete project
    public boolean deleteProject(String id) throws InterruptedException {
        logger.info("Deleting project with ID: " + id);
        try {
            DatabaseReference projectRef = firebaseDatabase.getReference("projects").child(id);
            
            // Check if project exists before deletion
            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] exists = {false};
            
            projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                logger.warning("Project with ID " + id + " not found for deletion");
                return false;
            }
            
            // Delete the project
            projectRef.removeValueAsync();
            logger.info("Project deleted successfully");
            return true;
            
        } catch (Exception e) {
            logger.severe("Error deleting project: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error deleting project", e);
        }
    }
}