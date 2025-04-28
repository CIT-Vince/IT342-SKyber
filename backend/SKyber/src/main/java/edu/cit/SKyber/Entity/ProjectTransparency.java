package edu.cit.SKyber.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTransparency {

    @Id
    private String id;

    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;  // For example: "Ongoing", "Completed", etc.
    private String projectManager; 
    private String teamMembers;  // List of team members or roles
    private String stakeholders;  // External stakeholders
    private String sustainabilityGoals;  // Sustainability goals of the project
    private String projectImage;

    
    
    private String budget;
}
