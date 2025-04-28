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
public class Volunteer {
    @Id
    private String id;
    private String title;
    private String description;
    private String registerLink;
    private String category;  
    private String location; 
    private LocalDate eventDate;
    private String contactPerson; 
    private String contactEmail;
    private String status;
    private String requirements; 
    private String volunteerImage;
}
