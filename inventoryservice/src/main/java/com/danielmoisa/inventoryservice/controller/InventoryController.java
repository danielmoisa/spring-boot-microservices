package com.danielmoisa.inventoryservice.controller;

import com.danielmoisa.inventoryservice.response.EventInventoryResponse;
import com.danielmoisa.inventoryservice.response.VenueInventoryResponse;
import com.danielmoisa.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(final InventoryService inventoryService){
    this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/events")
    public @ResponseBody List<EventInventoryResponse> inventoryGetAllEvents() {
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/venue/{venueId}")
    public @ResponseBody VenueInventoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId) {
        return inventoryService.getVenueInformation(venueId);
    }

    @GetMapping("/inventory/event/{eventId}")
    public @ResponseBody EventInventoryResponse inventoryForEvent (@PathVariable("eventId") Long eventId) {
        return inventoryService.getEventInventory(eventId);
    }

    @PutMapping("/inventory/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(
            @PathVariable("eventId") Long eventId,
            @PathVariable("capacity") Long capacity) {
        inventoryService.updateEventCapacity(eventId, capacity);
        return ResponseEntity.ok().build();
    }

}
