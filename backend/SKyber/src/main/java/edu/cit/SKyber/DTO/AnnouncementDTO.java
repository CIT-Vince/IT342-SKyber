package edu.cit.SKyber.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AnnouncementDTO{
    private String title;
    private String content;
    private String barangay;
    private String category;
    private List<MultipartFile> images; // uploaded image files
}
