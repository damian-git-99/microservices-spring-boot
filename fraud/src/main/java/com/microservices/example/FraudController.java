package com.microservices.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fraud-check")
public class FraudController {
    private final FraudCheckService fraudCheckService;

    @Autowired
    public FraudController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @GetMapping("/{customerId}")
    public boolean isFraud(@PathVariable Integer customerId) {
        return fraudCheckService.isFraud(customerId);
    }
}
