package edu.cit.SKyber.Service;

import edu.cit.SKyber.Entity.ProjectTransparency;
import edu.cit.SKyber.Repository.ProjectTransparencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectTransparencyService {

    private final ProjectTransparencyRepository repository;

    @Autowired
    public ProjectTransparencyService(ProjectTransparencyRepository repository) {
        this.repository = repository;
    }

    public List<ProjectTransparency> getAllProjects() {
        return repository.findAll();
    }

    public Optional<ProjectTransparency> getProjectById(Long id) {
        return repository.findById(id);
    }

    public ProjectTransparency createProject(ProjectTransparency projectTransparency) {
        return repository.save(projectTransparency);
    }

    public ProjectTransparency updateProject(Long id, ProjectTransparency updatedProject) {
        if (repository.existsById(id)) {
            updatedProject.setId(id);
            return repository.save(updatedProject);
        }
        return null;
    }

    public boolean deleteProject(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
