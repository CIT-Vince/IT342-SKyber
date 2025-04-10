package edu.cit.SKyber.Controller;

import edu.cit.SKyber.Entity.Announcement;
import edu.cit.SKyber.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Fetch all announcements
    @GetMapping
    public List<Announcement> getAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    // Fetch a single announcement by ID
    @GetMapping("/{id}")
    public Announcement getAnnouncementById(@PathVariable("id") Long id) {
        return announcementService.getAnnouncementById(id);
    }

    // Create a new announcement
    @PostMapping
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
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
    }
}
