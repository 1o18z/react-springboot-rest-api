package com.example.gccoffee.controller.api;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

  private final OrderService orderService;

  public OrderRestController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("api/v1/orders")
  public Order createOrder(CreateOrderRequest orderRequest){
    return orderService.createOrder(
            new Email(orderRequest.email()),
            orderRequest.address(),
            orderRequest.postcode(),
            orderRequest.orderItems()
    );
  }
}
