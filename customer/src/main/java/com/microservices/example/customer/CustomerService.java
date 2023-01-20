package com.microservices.example.customer;

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

        boolean response = restTemplate.getForObject(
                "http://localhost:8081/api/v1/fraud-check/" + customer.getId(),
                Boolean.class);

        if (response) {
            throw new IllegalStateException("Customer is a fraud");
        }
    }
}
