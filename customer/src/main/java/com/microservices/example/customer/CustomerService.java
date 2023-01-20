package com.microservices.example.customer;

import com.microservices.example.common.clients.fraud.FraudClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;

    @Autowired
    public CustomerService(CustomerRepository customerRepository
            , FraudClient fraudClient) {
        this.customerRepository = customerRepository;
        this.fraudClient = fraudClient;
    }
    public void registerCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .build();
        customerRepository.saveAndFlush(customer);

        var response = fraudClient.isFraudster(customer.getId());

        if (response == null || response.isFraud()) {
            throw new IllegalStateException("Customer is a fraud");
        }
    }
}
