package com.example.gccoffee.repository;

import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrderJdbcRepository implements OrderRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Order insert(Order order) {
    jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                    "VALUES(UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
            toOrderParamMap(order));
    order.getOrderItems()
            .forEach(item -> jdbcTemplate.update("INSERT INTO order_items(seq, order_id, product_id, category, price, quantity, created_at, updated_at)" +
                            "VALUES(UUID_TO_BIN(:orderId), :productId, :category, :price, :quantity, :createdAt, :updatedAt)",
                    toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
    return order;
  }

  private Map<String, Object> toOrderParamMap(Order order) {
    var paraMap = new HashMap<String, Object>();
    paraMap.put("orderId", order.getOrderId().toString().getBytes());
    paraMap.put("email", order.getEmail().getAddress());
    paraMap.put("address", order.getAddress());
    paraMap.put("postcode", order.getPostcode());
    paraMap.put("orderStatus", order.getOrderStatus().toString());
    paraMap.put("createdAt", order.getCreatedAt());
    paraMap.put("updatedAt", order.getUpdatedAt());
    return paraMap;
  }

  private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
    var paraMap = new HashMap<String, Object>();
    paraMap.put("orderId", orderId.toString().getBytes());
    paraMap.put("productId", item.productId().toString().getBytes());
    paraMap.put("category", item.category().toString());
    paraMap.put("price", item.price());
    paraMap.put("quantity", item.quantity());
    paraMap.put("created_at", createdAt);
    paraMap.put("updatedAt", updatedAt);
    return paraMap;
  }

}
