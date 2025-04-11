package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Announcement;
import edu.cit.SKyber.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // Serve the HTML form for creating an announcement at '/create-announcement'
    @GetMapping("/create-announcement")
    public String showAnnouncementForm() {
        return "announcement-form"; // This resolves to 'src/main/resources/templates/announcement-form.html'
    }

    // Fetch all announcements
    @GetMapping("/get-announcements")
    public List<Announcement> getAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    // Fetch a single announcement by ID
    @GetMapping("/get-announcement/{id}")
    public Announcement getAnnouncementById(@PathVariable("id") Long id) {
        return announcementService.getAnnouncementById(id);
    }

    // Create a new announcement
    @PostMapping("/create-announcement")
    public Announcement createAnnouncement(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("barangay") String barangay,
            @RequestParam("category") String category,
            @RequestParam("images") MultipartFile[] images) throws IOException {

        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setBarangay(barangay);
        announcement.setCategory(category);
        announcement.setPostedAt(LocalDateTime.now());

        // Handle image saving (uploading to disk or cloud)
        return announcementService.saveAnnouncement(announcement);
    }

    // Update an existing announcement
    @PutMapping("/update-announcement/{id}")
    public Announcement updateAnnouncement(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("barangay") String barangay,
            @RequestParam("category") String category,
            @RequestParam("images") MultipartFile[] images) throws IOException {

        Announcement announcement = announcementService.getAnnouncementById(id);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setBarangay(barangay);
        announcement.setCategory(category);
        announcement.setPostedAt(LocalDateTime.now());

        return announcementService.saveAnnouncement(announcement);
    }

    // Delete an announcement by ID
    @DeleteMapping("/delete-announcement/{id}")
    public void deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
    }
}
