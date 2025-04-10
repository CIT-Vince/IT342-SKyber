package edu.cit.SKyber.Service;

import edu.cit.SKyber.Entity.Announcement;
import edu.cit.SKyber.Repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    // Get all announcements
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    // Get announcement by ID
    public Announcement getAnnouncementById(Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        return announcement.orElse(null);  // Return the announcement if present, otherwise return null
    }

    // Save a new or updated announcement
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // Delete an announcement by ID
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
