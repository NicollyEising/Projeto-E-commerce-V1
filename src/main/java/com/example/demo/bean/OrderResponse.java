package com.example.demo.bean;

public class OrderResponse {
    private String orderId;
    private String approvalUrl;

    // Construtor
    public OrderResponse(String orderId, String approvalUrl) {
        this.orderId = orderId;
        this.approvalUrl = approvalUrl;
    }

    // Getters e Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getApprovalUrl() {
        return approvalUrl;
    }

    public void setApprovalUrl(String approvalUrl) {
        this.approvalUrl = approvalUrl;
    }
}
