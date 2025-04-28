package edu.cit.SKyber.Entity;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListing {
    @Id
    private String id;
    private String jobTitle;
    private String companyName;
    private String address; 
    private String description;
    private String applicationlink;  
    private String jobImage;
    private String employementType; 
}
