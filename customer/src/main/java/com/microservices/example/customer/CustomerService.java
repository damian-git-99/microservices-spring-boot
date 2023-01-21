package com.microservices.example.customer;

import com.microservices.example.common.clients.fraud.FraudClient;
import com.microservices.example.common.clients.notification.NotificationClient;
import com.microservices.example.common.clients.notification.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    @Autowired
    public CustomerService(CustomerRepository customerRepository
            , FraudClient fraudClient
            , NotificationClient notificationClient) {
        this.customerRepository = customerRepository;
        this.fraudClient = fraudClient;
        this.notificationClient = notificationClient;
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

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to our service...",
                        customer.getFirstName())
        );

        notificationClient.sendNotification(notificationRequest);
    }
}
