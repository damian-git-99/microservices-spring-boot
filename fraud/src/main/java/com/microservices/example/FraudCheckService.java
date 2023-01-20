package com.microservices.example;

import org.springframework.stereotype.Component;

@Component
public class FraudCheckService {
    public boolean isFraud(Integer customerId) {
        return false;
    }
}
