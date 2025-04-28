package edu.cit.SKyber.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SKProfile {

    @Id
    private String uid; // Use UID as the primary key instead of auto-incremented id
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String term; //ex 2015-2018
    private String platform;
    private String birthdate;
    private String gender;
    private int age;
    private String phoneNumber;
    private String address;
    private String skImage;
    private String role="ADMIN"; // Added role field for user roles (Optional)

}

