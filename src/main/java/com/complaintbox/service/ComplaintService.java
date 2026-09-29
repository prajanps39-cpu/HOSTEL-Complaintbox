package com.complaintbox.service;

import com.complaintbox.entity.Complaint;
import com.complaintbox.repository.ComplaintRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public Complaint createComplaint(Complaint complaint) {

        if (complaint.getStatus() == null || complaint.getStatus().isBlank()) {
            complaint.setStatus("OPEN");
        }

        complaint.setCreatedDate(LocalDateTime.now());
        complaint.setOverdue(false);

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public Complaint updateComplaint(Long id, Complaint updatedComplaint) {

        Complaint existingComplaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        String oldStatus = existingComplaint.getStatus();
        String newStatus = updatedComplaint.getStatus();

        if (!oldStatus.equals(newStatus)) {

            if (oldStatus.equals("OPEN")
                    && !newStatus.equals("IN_PROGRESS")) {

                throw new RuntimeException(
                        "Invalid status change. OPEN can only change to IN_PROGRESS");
            }

            if (oldStatus.equals("IN_PROGRESS")
                    && !newStatus.equals("RESOLVED")) {

                throw new RuntimeException(
                        "Invalid status change. IN_PROGRESS can only change to RESOLVED");
            }
        }

        existingComplaint.setStudentName(updatedComplaint.getStudentName());
        existingComplaint.setEmail(updatedComplaint.getEmail());
        existingComplaint.setRoomNumber(updatedComplaint.getRoomNumber());
        existingComplaint.setCategory(updatedComplaint.getCategory());
        existingComplaint.setSubject(updatedComplaint.getSubject());
        existingComplaint.setDescription(updatedComplaint.getDescription());
        existingComplaint.setStatus(newStatus);

        return complaintRepository.save(existingComplaint);
    }

    public List<Complaint> getOpenComplaintsSortedByAge() {

        return complaintRepository.findAll()
                .stream()
                .filter(complaint -> "OPEN".equals(complaint.getStatus()))
                .sorted(Comparator.comparing(
                        Complaint::getCreatedDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .toList();
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 3600000)
    public void checkOverdueComplaints() {

        List<Complaint> complaints = complaintRepository.findAll();

        LocalDateTime fiveDaysAgo =
                LocalDateTime.now().minusDays(5);

        for (Complaint complaint : complaints) {

            if ("OPEN".equals(complaint.getStatus())
                    && complaint.getCreatedDate() != null
                    && complaint.getCreatedDate().isBefore(fiveDaysAgo)) {

                complaint.setOverdue(true);
                complaintRepository.save(complaint);
            }
        }
    }
}