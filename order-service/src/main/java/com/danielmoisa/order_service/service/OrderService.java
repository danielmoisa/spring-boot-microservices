package com.danielmoisa.order_service.service;

import com.danielmoisa.bookingservice.event.BookingEvent;
import com.danielmoisa.order_service.client.InventoryServiceClient;
import com.danielmoisa.order_service.entity.Order;
import com.danielmoisa.order_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    private OrderRepository orderRepository;
    private InventoryServiceClient inventoryServiceClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        InventoryServiceClient inventoryServiceClient)  {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Received booking event: {}", bookingEvent);

        // Create object for database
        Order order = createOrder(bookingEvent);

        // Save order to database
        orderRepository.saveAndFlush(order);

        // Update inventory
        inventoryServiceClient.updateInventory(
                order.getEventId(), order.getTicketCount()
        );
        log.info("Order created and inventory updated for event ID: {}", order.getEventId());
    }

    private Order createOrder(BookingEvent bookingEvent) {
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount( bookingEvent.getTicketCount())
                .totalPrice( bookingEvent.getTotalPrice())
                .build();
    }
}
