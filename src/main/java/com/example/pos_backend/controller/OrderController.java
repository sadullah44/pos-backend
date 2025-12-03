package com.example.pos_backend.controller;

import com.example.pos_backend.dto.AddOrderItemRequest;
import com.example.pos_backend.dto.CreateOrderRequest;
import com.example.pos_backend.model.Order;
import com.example.pos_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/siparisler")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createNewOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.getTableId(), request.getWaiterId());
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderID}")
    public Order getOrderById(@PathVariable Long orderID) {
        return orderService.getOrderById(orderID);
    }

    @PutMapping("/{orderID}/durum")
    public Order updateOrderStatus(
            @PathVariable Long orderID,
            @RequestParam String yeniDurum
    ) {
        return orderService.updateOrderStatus(orderID, yeniDurum);
    }

    @PostMapping("/{orderId}/urunEkle")
    public Order addOrderItemToOrder(
            @PathVariable Long orderId,
            @RequestBody AddOrderItemRequest request
    ) {
        return orderService.addOrderItemToOrder(orderId, request);
    }

    @GetMapping("/masa/{tableId}")
    public Order getOrderByTable(@PathVariable Long tableId) {
        return orderService.getActiveOrderByTableId(tableId);
    }
    @DeleteMapping("/{orderId}/urun/{orderItemId}")
    public Order removeOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long orderItemId
    ) {
        return orderService.removeOrderItem(orderId, orderItemId);
    }

}
