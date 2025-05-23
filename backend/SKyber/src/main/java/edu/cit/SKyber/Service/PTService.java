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
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
    DateTimeFormatter.ISO_LOCAL_DATE,         // yyyy-MM-dd
    DateTimeFormatter.ofPattern("MM/dd/yyyy") // MM/dd/yyyy
    };

    // Add this method to your PTService class
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                // Try next formatter
            }
        }
        
        logger.warning("Could not parse date: " + dateStr);
        return null;
    }
    
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    // Modified createProject method with better date handling
    public ProjectTransparency createProject(ProjectTransparency project) {
        try {
            logger.info("Creating new project with name: " + project.getProjectName());
            
            // Create a map for Firebase compatibility
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("projectName", project.getProjectName());
            projectMap.put("description", project.getDescription());
            projectMap.put("budget", project.getBudget());
            
            // Date handling with null check and better formatting
            if (project.getStartDate() != null) {
                try {
                    projectMap.put("startDate", project.getStartDate().format(DATE_FORMATTERS[0]));
                    logger.info("Formatted start date: " + project.getStartDate().format(DATE_FORMATTERS[0]));
                } catch (Exception e) {
                    logger.warning("Error formatting start date: " + e.getMessage());
                    projectMap.put("startDate", "");
                }
            } else {
                logger.info("No start date provided");
                projectMap.put("startDate", "");
            }
            
            if (project.getEndDate() != null) {
                try {
                    projectMap.put("endDate", project.getEndDate().format(DATE_FORMATTERS[0]));
                    logger.info("Formatted end date: " + project.getEndDate().format(DATE_FORMATTERS[0]));
                } catch (Exception e) {
                    logger.warning("Error formatting end date: " + e.getMessage());
                    projectMap.put("endDate", "");
                }
            } else {
                logger.info("No end date provided");
                projectMap.put("endDate", "");
            }
            
            // Other fields with null checks
            projectMap.put("status", project.getStatus() != null ? project.getStatus() : "");
            projectMap.put("projectManager", project.getProjectManager() != null ? project.getProjectManager() : "");
            projectMap.put("teamMembers", project.getTeamMembers() != null ? project.getTeamMembers() : "");
            projectMap.put("stakeholders", project.getStakeholders() != null ? project.getStakeholders() : "");
            projectMap.put("sustainabilityGoals", project.getSustainabilityGoals() != null ? project.getSustainabilityGoals() : "");
            
            // Store image if available
            if (project.getProjectImage() != null && !project.getProjectImage().isEmpty()) {
                projectMap.put("projectImage", project.getProjectImage());
            }
            
            // Push to Firebase and get key
            DatabaseReference projectRef = firebaseDatabase.getReference("ProjectTransparency").push();
            String key = projectRef.getKey();
            logger.info("Generated Firebase key: " + key);
            
            // Store the key as id in the map and object
            projectMap.put("id", key);
            project.setId(key); // Use hashCode for numeric ID
            
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
        project.setBudget(budget != null ? budget.toString() : null);
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
            DatabaseReference projectsRef = firebaseDatabase.getReference("ProjectTransparency");
            projectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        logger.info("Retrieved " + snapshot.getChildrenCount() + " projects from Firebase");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            try {
                                String key = dataSnapshot.getKey();
                                logger.info("Processing project with key: " + key);
                                
                                // Create a new project
                                ProjectTransparency project = new ProjectTransparency();
                                
                                // Use the Firebase key directly as the ID string
                                // Don't convert to numeric ID which causes issues
                                project.setId(key); // Use hashCode for numeric ID                                
                                // Map fields from Firebase to entity
                                if (dataSnapshot.hasChild("projectName")) {
                                    project.setProjectName(dataSnapshot.child("projectName").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("description")) {
                                    project.setDescription(dataSnapshot.child("description").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("budget")) {
                                    try {
                                        String rawBudgetValue = dataSnapshot.child("budget").getValue(String.class);
                                        logger.info("Raw budget value from Firebase: '" + rawBudgetValue + "' type: " + 
                                                   (rawBudgetValue != null ? rawBudgetValue.getClass().getName() : "null"));
                                        
                                        // Don't parse - just use the raw value
                                        project.setBudget(rawBudgetValue);
                                        
                                        // If you still want to normalize it:
                                        try {
                                            Double budgetNum = Double.parseDouble(rawBudgetValue);
                                            project.setBudget(String.valueOf(budgetNum));
                                        } catch (Exception e) {
                                            // If parsing fails, still use the original string
                                            logger.warning("Budget parsing as number failed, using original: " + rawBudgetValue);
                                        }
                                    } catch (Exception e) {
                                        logger.warning("Error processing budget: " + e.getMessage());
                                        project.setBudget("0.0");
                                    }
                                }
                                
                                // Convert string dates back to LocalDate
                                
                                if (dataSnapshot.hasChild("startDate")) {
                                    String dateStr = dataSnapshot.child("startDate").getValue(String.class);
                                    project.setStartDate(parseDate(dateStr));
                                }
                                
                                
                                if (dataSnapshot.hasChild("endDate")) {
                                    String dateStr = dataSnapshot.child("endDate").getValue(String.class);
                                    project.setEndDate(parseDate(dateStr));
}
                                
                                if (dataSnapshot.hasChild("status")) {
                                    project.setStatus(dataSnapshot.child("status").getValue(String.class));
                                }
                                
                                if (dataSnapshot.hasChild("projectManager")) {
                                    project.setProjectManager(dataSnapshot.child("projectManager").getValue(String.class));
                                }
                                
                                
                                if (dataSnapshot.hasChild("teamMembers")) {
                                    try {
                                        Object value = dataSnapshot.child("teamMembers").getValue();
                                        if (value instanceof ArrayList) {
                                            // Join array elements with commas
                                            ArrayList<String> list = (ArrayList<String>) value;
                                            project.setTeamMembers(String.join(", ", list));
                                        } else if (value instanceof String) {
                                            project.setTeamMembers((String) value);
                                        } else if (value != null) {
                                            project.setTeamMembers(value.toString());
                                        }
                                    } catch (Exception e) {
                                        logger.warning("Error converting teamMembers: " + e.getMessage());
                                    }
                                }
                                
                                if (dataSnapshot.hasChild("stakeholders")) {
                                    try {
                                        Object value = dataSnapshot.child("stakeholders").getValue();
                                        if (value instanceof ArrayList) {
                                            ArrayList<String> list = (ArrayList<String>) value;
                                            project.setStakeholders(String.join(", ", list));
                                        } else if (value instanceof String) {
                                            project.setStakeholders((String) value);
                                        } else if (value != null) {
                                            project.setStakeholders(value.toString());
                                        }
                                    } catch (Exception e) {
                                        logger.warning("Error converting stakeholders: " + e.getMessage());
                                    }}
                                
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
                                e.printStackTrace();
                                // Continue to next project
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
            
            logger.info("Successfully retrieved " + projects.size() + " projects");
            return projects;
            
        } catch (Exception e) {
            logger.severe("Error getting projects: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching projects", e);
        }
    }

    // Get project by ID (improved version that handles both string and numeric IDs)
    public ProjectTransparency getProjectById(String id) throws InterruptedException {
        logger.info("Fetching project with ID: " + id);
        
        // Debug info about the ID
        logger.info("ID type: " + (id == null ? "null" : id.getClass().getName()));
        logger.info("ID value: " + id);
        
        final CountDownLatch latch = new CountDownLatch(1);
        final ProjectTransparency[] project = new ProjectTransparency[1];
        final List<Exception> exceptions = new ArrayList<>();

        try {
            // Direct lookup using the provided ID
            DatabaseReference projectRef = firebaseDatabase.getReference("ProjectTransparency").child(id);
            
            projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            logger.warning("No project found with direct ID: " + id);
                            
                            // If direct lookup fails, log it but continue (we'll complete the latch)
                            latch.countDown();
                            return;
                        }
                        
                        String key = snapshot.getKey();
                        logger.info("Found project with key: " + key);
                        
                        // Create a new project
                        ProjectTransparency p = new ProjectTransparency();
                        p.setId(key);
                        
                        // Map fields from Firebase (existing code)
                        if (snapshot.hasChild("projectName")) {
                            p.setProjectName(snapshot.child("projectName").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("description")) {
                            p.setDescription(snapshot.child("description").getValue(String.class));
                        }
                        
                        if (snapshot.hasChild("budget")) {
                            try {
                                String rawBudgetValue = snapshot.child("budget").getValue(String.class);
                                logger.info("getProjectById - Raw budget value from Firebase: '" + rawBudgetValue + "'");
                                
                                // Don't parse - just use the raw value initially
                                p.setBudget(rawBudgetValue);
                                
                                // Only normalize if it's a valid number
                                try {
                                    Double budgetNum = Double.parseDouble(rawBudgetValue);
                                    p.setBudget(String.valueOf(budgetNum));
                                } catch (Exception e) {
                                    // If parsing fails, still use the original string
                                    logger.warning("Budget parsing as number failed, using original: " + rawBudgetValue);
                                }
                            } catch (Exception e) {
                                logger.warning("Error processing budget: " + e.getMessage());
                                // Use a more visible placeholder to help with debugging
                                p.setBudget("ERROR_PROCESSING_BUDGET");
                            }
                        }
                        
                        // Convert string dates back to LocalDate
                        if (snapshot.hasChild("startDate")) {
                            String dateStr = snapshot.child("startDate").getValue(String.class);
                            p.setStartDate(parseDate(dateStr));
                            try {
                                p.setStartDate(LocalDate.parse(dateStr, DATE_FORMATTERS[0]));
                            } catch (Exception e) {
                                logger.warning("Error parsing start date: " + dateStr);
                            }
                        }
                        
                        if (snapshot.hasChild("endDate")) {
                            String dateStr = snapshot.child("endDate").getValue(String.class);
                            p.setEndDate(parseDate(dateStr));
                            try {
                                p.setEndDate(LocalDate.parse(dateStr, DATE_FORMATTERS[0]));
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
                            try {
                                Object value = snapshot.child("teamMembers").getValue();
                                if (value instanceof ArrayList) {
                                    ArrayList<String> list = (ArrayList<String>) value;
                                    p.setTeamMembers(String.join(", ", list));
                                } else if (value instanceof String) {
                                    p.setTeamMembers((String) value);
                                } else if (value != null) {
                                    p.setTeamMembers(value.toString());
                                }
                            } catch (Exception e) {
                                logger.warning("Error converting teamMembers: " + e.getMessage());
                            }
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
                        logger.severe("Error processing project data: " + e.getMessage());
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
            
            if (project[0] == null) {
                logger.severe("Project not found with ID: " + id);
                throw new RuntimeException("Project not found with ID: " + id);
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
                updates.put("startDate", projectDetails.getStartDate().format(DATE_FORMATTERS[0]));
            }
            
            if (projectDetails.getEndDate() != null) {
                updates.put("endDate", projectDetails.getEndDate().format(DATE_FORMATTERS[0]));
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
            DatabaseReference projectRef = firebaseDatabase.getReference("ProjectTransparency").child(id);
            projectRef.updateChildrenAsync(updates);
            
            // Set the ID in the returned object for consistency
            projectDetails.setId(id);
            
            logger.info("Project updated successfully");
            return projectDetails;
            
        } catch (Exception e) {
            logger.severe("Error updating project: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating project", e);
        }
    }

    // Update project image - improved version
    public ProjectTransparency updateProjectImage(String id, MultipartFile image) throws IOException, InterruptedException {
        logger.info("Updating image for project with ID: " + id);
        
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
            updates.put("projectImage", imageData);
            
            DatabaseReference projectRef = firebaseDatabase.getReference("ProjectTransparency").child(id);
            projectRef.updateChildrenAsync(updates);
            logger.info("Firebase update initiated for project: " + id);
            
            // Also fetch the existing project to return updated data
            ProjectTransparency existingProject;
            try {
                existingProject = getProjectById(id);
                if (existingProject == null) {
                    throw new RuntimeException("Project not found with ID: " + id);
                }
                
                // Update the returned object with new image
                existingProject.setProjectImage(imageData);
                logger.info("Image updated successfully for project: " + id);
                
                return existingProject;
            } catch (Exception e) {
                logger.severe("Error fetching project after image update: " + e.getMessage());
                
                // Create a minimal response with just the image
                ProjectTransparency response = new ProjectTransparency();
                response.setId(id);
                response.setProjectImage(imageData);
                
                return response;
            }
        } catch (IOException e) {
            logger.severe("Error processing image: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.severe("Unexpected error updating image: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating project image", e);
        }
    }

    // Delete project
    public boolean deleteProject(String id) throws InterruptedException {
        logger.info("Deleting project with ID: " + id);
        try {
            DatabaseReference projectRef = firebaseDatabase.getReference("ProjectTransparency").child(id);
            
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