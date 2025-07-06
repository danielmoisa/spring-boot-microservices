package com.danielmoisa.bookingservice.service;

import com.danielmoisa.bookingservice.client.InventoryServiceClient;
import com.danielmoisa.bookingservice.entity.Customer;
import com.danielmoisa.bookingservice.repository.CustomerRepository;
import com.danielmoisa.bookingservice.request.BookingRequest;
import com.danielmoisa.bookingservice.response.BookingResponse;
import com.danielmoisa.bookingservice.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public BookingService(final CustomerRepository customerRepository, final InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
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



        return BookingResponse.builder().build();
    }
}
