package com.microservices.example;

import com.microservices.example.common.clients.fraud.FraudCheckResponse;
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
    public FraudCheckResponse isFraud(@PathVariable Integer customerId) {
        boolean result = fraudCheckService.isFraud(customerId);
        return new FraudCheckResponse(result);
    }
}
