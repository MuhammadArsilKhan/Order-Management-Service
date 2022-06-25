package com.renergy.ordermanagementservice.api;

//import com.nimbusds.jose.util.Base64URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.renergy.ordermanagementservice.order.Order;
import com.renergy.ordermanagementservice.order.OrderRepository;
import com.renergy.ordermanagementservice.orderProduct.OrderProduct;
import com.renergy.ordermanagementservice.orderProduct.OrderProductRepository;
import com.renergy.ordermanagementservice.pojo.CurrentUser;
import com.renergy.ordermanagementservice.responses.DefaultResponse;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    public DefaultResponse createNewOrder(Order order) {

        double orderTotal = 0;
        order.setActive(true);
        order.setCreatedDate(new Date());
        for(OrderProduct orderProduct : order.getOrderProductList()){
            orderProduct.setOrder(order);
            orderProduct.setTotalPrice(orderProduct.getUnitPrice() * orderProduct.getQuantity());
            orderTotal += orderProduct.getTotalPrice();
        }
        order.setTotalAmount(orderTotal);
        orderRepository.save(order);
        orderProductRepository.saveAll(order.getOrderProductList());
        Optional<Order> order1 = orderRepository.findFirstByOrderByIdDesc();
        order1.get().setOrderNumber("ORD-" + order1.get().getId());
        orderRepository.save(order1.get());
        return new DefaultResponse("S001", "Order created successfully");
    }

    public ResponseEntity<?> getOrderByOrderNumber(String orderNumber) throws ParseException, NoSuchFieldException, IllegalAccessException {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").toString();
        System.out.println("token : " + token);
        token = token.replace("Bearer ", "");
        System.out.println("token : " + token);
        JWT jwt = JWTParser.parse(token);
        Base64URL[] parsedParts = jwt.getParsedParts();
        String payloadJson = StringUtils.newStringUtf8(Base64.decodeBase64(parsedParts[1].toString()));
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        CurrentUser currentUser = gson.fromJson(payloadJson, CurrentUser.class);
        Optional<Order> order = orderRepository.findOrderByOrderNumberAndUserName(orderNumber, currentUser.getEmail());
        if(order.isPresent()) {
            return new ResponseEntity(order, HttpStatus.OK);
        }
        return new ResponseEntity(new DefaultResponse("F001", "No order found"), HttpStatus.NO_CONTENT);
    }
}
