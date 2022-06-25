package com.renergy.ordermanagementservice.orderProduct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.renergy.ordermanagementservice.order.Order;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_order_product")
@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String productUUID;

    @NonNull
    private int quantity;

    private double unitPrice;

    private double totalPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;
}
