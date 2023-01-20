package com.microservices.example.customer;

import com.microservices.example.common.clients.fraud.FraudCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerService(CustomerRepository customerRepository
            , RestTemplate restTemplate) {
        this.customerRepository = customerRepository;
        this.restTemplate = restTemplate;
    }
    public void registerCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .build();
        customerRepository.saveAndFlush(customer);

        var response = restTemplate.getForObject(
                "http://fraud/api/v1/fraud-check/" + customer.getId(),
                FraudCheckResponse.class);

        if (response == null || response.isFraud()) {
            throw new IllegalStateException("Customer is a fraud");
        }
    }
}
