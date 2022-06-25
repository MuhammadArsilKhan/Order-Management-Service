package com.renergy.ordermanagementservice.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    Order findFirstByIdOrderByIdDesc();

    Optional<Order> findFirstByOrderByIdDesc();

    Optional<Order> findOrderByOrderNumberAndUserName(String orderNumber, String username);
}
