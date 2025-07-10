package com.danielmoisa.bookingservice.service;

import com.danielmoisa.bookingservice.client.InventoryServiceClient;
import com.danielmoisa.bookingservice.dto.CustomerDTO;
import com.danielmoisa.bookingservice.entity.Customer;
import com.danielmoisa.bookingservice.event.BookingEvent;
import com.danielmoisa.bookingservice.repository.CustomerRepository;
import com.danielmoisa.bookingservice.request.BookingRequest;
import com.danielmoisa.bookingservice.response.BookingResponse;
import com.danielmoisa.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {
    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(final CustomerRepository customerRepository, final InventoryServiceClient inventoryServiceClient, final KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        final Customer customer = customerRepository.findById(request.getUserId()).orElse(null);

        if (customer == null) {
            throw new RuntimeException("User not found");
        }

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());

        if (inventoryResponse.getCapacity() < request.getTicketCount()) {
            throw new RuntimeException("Not enough inventory");
        }

        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);

        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking event sent to Kafka: {}", bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(final BookingRequest request, final Customer customer, final InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }

    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        return new CustomerDTO(customer.getId(), customer.getEmail(), customer.getAddress());
    }
}
