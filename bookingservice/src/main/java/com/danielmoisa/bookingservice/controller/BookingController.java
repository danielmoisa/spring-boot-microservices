package com.danielmoisa.bookingservice.controller;

import com.danielmoisa.bookingservice.dto.CustomerDTO;
import com.danielmoisa.bookingservice.request.BookingRequest;
import com.danielmoisa.bookingservice.response.BookingResponse;
import com.danielmoisa.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json", path = "/booking")
    public BookingResponse createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return bookingService.getCustomerById(id);
    }
}
