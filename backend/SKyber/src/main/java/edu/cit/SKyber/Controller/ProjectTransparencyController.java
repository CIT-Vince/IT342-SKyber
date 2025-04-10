package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.ProjectTransparency;
import edu.cit.SKyber.Service.ProjectTransparencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-transparency")
public class ProjectTransparencyController {

    private final ProjectTransparencyService projectTransparencyService;

    @Autowired
    public ProjectTransparencyController(ProjectTransparencyService projectTransparencyService) {
        this.projectTransparencyService = projectTransparencyService;
    }

    // Get all projects
    @GetMapping
    public List<ProjectTransparency> getAllProjects() {
        return projectTransparencyService.getAllProjects();
    }

    // Get a single project by ID
    @GetMapping("/{id}")
    public Optional<ProjectTransparency> getProjectById(@PathVariable Long id) {
        return projectTransparencyService.getProjectById(id);
    }

    // Create a new project
    @PostMapping
    public ProjectTransparency createProject(@RequestBody ProjectTransparency projectTransparency) {
        return projectTransparencyService.createProject(projectTransparency);
    }

    // Update an existing project
    @PutMapping("/{id}")
    public ProjectTransparency updateProject(@PathVariable Long id, @RequestBody ProjectTransparency updatedProject) {
        return projectTransparencyService.updateProject(id, updatedProject);
    }

    // Delete a project by ID
    @DeleteMapping("/{id}")
    public boolean deleteProject(@PathVariable Long id) {
        return projectTransparencyService.deleteProject(id);
    }
}
