package com.complaintbox.controller;

import com.complaintbox.entity.Complaint;
import com.complaintbox.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @PutMapping("/{id}")
    public Complaint updateComplaint(
            @PathVariable Long id,
            @RequestBody Complaint complaint) {

        return complaintService.updateComplaint(id, complaint);
    }

    @GetMapping("/warden/open")
    public List<Complaint> getOpenComplaintsSortedByAge() {
        return complaintService.getOpenComplaintsSortedByAge();
    }

    @DeleteMapping("/{id}")
    public String deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return "Complaint deleted successfully";
    }
}