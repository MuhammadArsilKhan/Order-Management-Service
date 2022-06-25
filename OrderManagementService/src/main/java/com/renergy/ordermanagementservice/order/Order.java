package com.renergy.ordermanagementservice.order;

import com.renergy.ordermanagementservice.orderProduct.OrderProduct;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_order")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderNumber;

    private double totalAmount;

    private Date createdDate;

    private Date deliveryDate;

    private String paymentId;

    private boolean active;

    @NonNull
    private String userName;

    @OneToMany(mappedBy = "order" , fetch = FetchType.EAGER)
    private List<OrderProduct> orderProductList = new ArrayList<>();

}
