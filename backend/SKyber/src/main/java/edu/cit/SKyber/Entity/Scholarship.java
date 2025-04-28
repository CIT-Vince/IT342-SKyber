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
public class Scholarship {
    @Id
    private String id;
    private String title;
    private String description;
    private String link;  
    private String contactEmail; 
    private String type;  
    private String scholarImage;
}
