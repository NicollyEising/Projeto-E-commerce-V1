package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ApprovalUrlService {
    
    // Private field to store the approval URL
    private String storedApprovalUrl;

    // Setter method to store the approval URL
    public void storeApprovalUrl(String approvalUrl) {
        this.storedApprovalUrl = approvalUrl;
    }

    // Private field for approval URL (not used in this context, can be removed if not needed)
    private String approvalUrl;

    // Setter method for approval URL (not used in this context, can be removed if not needed)
    public void setApprovalUrl(String approvalUrl) {
        this.approvalUrl = approvalUrl;
    }

    // Getter method to retrieve the stored approval URL
    public String getApprovalUrl() {
        return storedApprovalUrl;
    }
}
