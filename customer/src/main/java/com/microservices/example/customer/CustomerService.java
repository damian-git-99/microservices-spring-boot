package com.microservices.example.customer;

import com.microservices.example.broker.RabbitMQMessageProducer;
import com.microservices.example.common.clients.fraud.FraudCheckResponse;
import com.microservices.example.common.clients.fraud.FraudClient;
import com.microservices.example.common.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;


    public void registerCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse response = fraudClient.isFraudster(customer.getId());

        log.info("Fraud check for customer id {} returned {}", customer.getId(), response.isFraud());

        if (response.isFraud()) {
            throw new IllegalStateException("Customer is a fraud");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to our service...",
                        customer.getFirstName())
        );

        log.info("Sending notification {}", notificationRequest);
        rabbitMQMessageProducer.publish(
                "internal.notification",
                "internal.notification.routing.key",
                notificationRequest
        );
    }
}
