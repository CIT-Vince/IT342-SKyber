package edu.cit.SKyber.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

    @Id
    private String id; // Use UID as the primary key instead of auto-incremented id

    private String firstName;
    private String lastName;
    private String age;
    private String address;
    private String partylist;
    private String platform;
    private String candidateImage;
   
}
