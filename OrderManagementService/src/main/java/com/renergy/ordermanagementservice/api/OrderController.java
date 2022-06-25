package com.renergy.ordermanagementservice.api;

import com.renergy.ordermanagementservice.order.Order;
import com.renergy.ordermanagementservice.responses.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping(path = "/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.POST, value="")
    public @ResponseBody
    DefaultResponse createNewOrder(@RequestBody Order order) {
        return orderService.createNewOrder(order);
    }

    @RequestMapping(method = RequestMethod.GET, value="")
    public @ResponseBody
    ResponseEntity<?> getOrderByOrderNumber(@RequestParam("orderNumber") String orderNumber) throws ParseException, NoSuchFieldException, IllegalAccessException {
        return orderService.getOrderByOrderNumber(orderNumber);
    }


}
