package com.microservices.example.common.clients.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationRequest {
    private Integer toCustomerId;
    private String toCustomerName;
    private String message;
}
