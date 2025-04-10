package edu.cit.SKyber.Repository;

import edu.cit.SKyber.Entity.ProjectTransparency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTransparencyRepository extends JpaRepository<ProjectTransparency, Long> {
}
