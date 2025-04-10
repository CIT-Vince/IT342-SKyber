package edu.cit.SKyber.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String barangay; // for geofencing
    private String category;

    @JsonFormat(pattern = "MMMM d, yyyy h:mm a")
    private LocalDateTime postedAt;

    @ElementCollection
    private List<String> imageUrls;
}
