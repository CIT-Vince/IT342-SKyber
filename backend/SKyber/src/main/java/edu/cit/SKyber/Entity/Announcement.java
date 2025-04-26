package edu.cit.SKyber.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement implements Serializable {
    
    private Long id;
    private String title;
    private String content;
    private String barangay;
    private String category;
    private String imageData;
    
    @JsonFormat(pattern = "MMMM d, yyyy h:mm a")
    private LocalDateTime postedAt;
    
    // Default no-arg constructor required by Firebase
    
    // Add getter/setter for string version of postedAt for Firebase compatibility
    public String getPostedAtString() {
        return postedAt != null ? postedAt.toString() : null;
    }
    
    public void setPostedAtString(String postedAtStr) {
        // This method will be used by Firebase for deserialization
    }
}