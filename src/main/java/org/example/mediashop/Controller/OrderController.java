package org.example.mediashop.Controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.ValueOfEnum;
import org.example.mediashop.Data.DTO.OrderDTO;
import org.example.mediashop.Data.Entity.OrderStatus;
import org.example.mediashop.Data.Entity.UserDetailsImpl;
import org.example.mediashop.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    /**
     * This method retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order. It must be a positive number.
     * @return A ResponseEntity containing a map with a single key-value pair: "order" and an OrderDTO object.
     * @throws ConstraintViolationException If the provided id is not a positive number.
     */
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, OrderDTO>> getOrderById(@PathVariable("id") @Positive final Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("order", orderDTO));
    }

    /**
     * This method retrieves a list of orders based on their status.
     *
     * @param status The status of the orders to retrieve.
     * @return A ResponseEntity containing a map with a single key-value pair: "order" and a list of OrderDTO objects.
     * @throws ConstraintViolationException If the provided status is not one of the valid options.
     */
    @GetMapping(value = "/status/{status}")
    public ResponseEntity<Map<String, List<OrderDTO>>> getOrdersByStatus(@PathVariable("status") @ValueOfEnum(enumClass = OrderStatus.class) final String status) {
        List<OrderDTO> orderDTO = orderService.getOrderByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("order", orderDTO));
    }

    /**
     * This method retrieves a list of orders based on their status and user id.
     *
     * @param status The status of the orders to retrieve.
     * @param userId The unique identifier of the user whose orders to retrieve. It must be a positive number.
     * @return A ResponseEntity containing a map with a single key-value pair: "order" and a list of OrderDTO objects.
     * @throws ConstraintViolationException If the provided status is not one of the valid options or if the provided userId is not a positive number.
     */
    @GetMapping(value = "/status/{status}/userId/{userId}")
    public ResponseEntity<Map<String, List<OrderDTO>>> getOrdersByStatusAndUserId(@PathVariable("status") @ValueOfEnum(enumClass = OrderStatus.class) final String status,
                                                                                  @PathVariable("userId") @Positive final Long userId) {
        List<OrderDTO> orderDTO = orderService.getOrderByStatusAndUserId(status, userId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("order", orderDTO));
    }

    /**
     * This method updates the status of an order based on its unique identifier and the new status.
     * Order can be updated only by owner.
     *
     * @param id     The unique identifier of the order to update. It must be a positive number.
     * @param status The new status of the order.
     * @return A ResponseEntity containing a map with a single key-value pair: "order" and an OrderDTO object.
     * @throws ConstraintViolationException If the provided id is not a positive number or if the provided status is not one of the valid options.
     */
    @PutMapping(value = "/id/{id}/status/{status}")
    public ResponseEntity<Map<String, OrderDTO>> updateOrderStatusById(@PathVariable("id") @Positive final Long id,
                                                                       @PathVariable("status") @Valid @ValueOfEnum(enumClass = OrderStatus.class) final String status,
                                                                       @AuthenticationPrincipal UserDetailsImpl principal) {
        OrderDTO order = orderService.updateOrderStatusById(id, status, principal);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("order", order));
    }

    /**
     * This method creates a new order based on the provided OrderDTO object.
     *
     * @param orderDTO The OrderDTO object containing the details of the new order.
     * @return A ResponseEntity containing a map with a single key-value pair: "order" and an OrderDTO object.
     * @throws ConstraintViolationException If the provided OrderDTO object does not meet the validation rules.
     */
    @PostMapping(value = "/new")
    public ResponseEntity<Map<String, OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO order = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("order", order));
    }
}
