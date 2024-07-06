package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.Order;
import org.example.mediashop.Data.Entity.OrderPaymentStatus;
import org.example.mediashop.Data.Entity.OrderStatus;
import org.example.mediashop.Data.Entity.User;
import org.example.mediashop.Repository.OrderRepository;
import org.example.mediashop.Repository.UserRepository;
import org.example.mediashop.TestConfig.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class OrderControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        orderRepository.deleteAll();
    }

    @Test
    void shouldGetOrderById() {
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order = orderRepository.save(new Order(user.getId(), 100.0, OrderStatus.COMPLETED, OrderPaymentStatus.PAID, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5)));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/id/{0}", order.getId().intValue())
                .then()
                .statusCode(200)
                .body("order.id", is(order.getId().intValue()))
                .body("order.totalAmount", is(order.getTotalAmount().floatValue()))
                .body("order.status", is(order.getStatus().name()))
                .body("order.paymentStatus", is(order.getPaymentStatus().name()))
                .body("order.paymentMethod", is(order.getPaymentMethod()))
                .body("order.shippingAddress", is(order.getShippingAddress()))
                .body("order.shippingMethod", is(order.getShippingMethod()))
                .body("order.orderDate", is(order.getOrderDate().toString()))
                .body("order.deliveryDate", is(order.getDeliveryDate().toString()));
    }

    @Test
    void getOrderById_shouldReturn400_whenOrderIdIsInvalid() {
        long orderId = 0L;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        orderRepository.save(new Order(user.getId(), 100.0, OrderStatus.COMPLETED, OrderPaymentStatus.PAID, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5)));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/id/{0}", orderId)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getOrderById.id: must be greater than " + orderId))
                .body("statusCode", is(400));
    }

    @Test
    void getOrderById_shouldReturn404_whenOrderIdNotFound() {
        long orderId = 10L;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        orderRepository.save(new Order(user.getId(), 100.0, OrderStatus.COMPLETED, OrderPaymentStatus.PAID, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5)));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/id/{0}", orderId)
                .then()
                .statusCode(404)
                .body("message", is("Order with id: " + orderId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetOrdersByStatus() {
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}", OrderStatus.CANCELLED)
                .then()
                .statusCode(200)
                .body("order.", hasSize(1))
                .body("order[0].id", is(order2.getId().intValue()))
                .body("order[0].totalAmount", is(order2.getTotalAmount().floatValue()))
                .body("order[0].status", is(order2.getStatus().name()))
                .body("order[0].paymentStatus", is(order2.getPaymentStatus().name()))
                .body("order[0].paymentMethod", is(order2.getPaymentMethod()))
                .body("order[0].shippingAddress", is(order2.getShippingAddress()))
                .body("order[0].shippingMethod", is(order2.getShippingMethod()))
                .body("order[0].orderDate", is(order2.getOrderDate().toString()))
                .body("order[0].deliveryDate", is(order2.getDeliveryDate().toString()));
    }

    @Test
    void getOrdersByStatus_shouldReturn400_whenOrderStatusIsInvalid() {
        String orderStatus = "InvalidOrderStatus";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}", orderStatus)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getOrdersByStatus.status: Invalid status"))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatus_shouldReturn404_whenOrderStatusNotFound() {
        OrderStatus orderStatus = OrderStatus.EXPIRED;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}", orderStatus)
                .then()
                .statusCode(404)
                .body("message", is("Order with status: " + orderStatus + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldGetOrdersByStatusAndUserId() {
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));


        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", OrderStatus.DELIVERED, order1.getUserId())
                .then()
                .statusCode(200)
                .body("order.", hasSize(1))
                .body("order[0].id", is(order1.getId().intValue()))
                .body("order[0].totalAmount", is(order1.getTotalAmount().floatValue()))
                .body("order[0].status", is(order1.getStatus().name()))
                .body("order[0].paymentStatus", is(order1.getPaymentStatus().name()))
                .body("order[0].paymentMethod", is(order1.getPaymentMethod()))
                .body("order[0].shippingAddress", is(order1.getShippingAddress()))
                .body("order[0].shippingMethod", is(order1.getShippingMethod()))
                .body("order[0].orderDate", is(order1.getOrderDate().toString()))
                .body("order[0].deliveryDate", is(order1.getDeliveryDate().toString()));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn400_whenOrderStatusIsInvalid() {
        String orderStatus = "InvalidOrderStatus";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", orderStatus, order1.getUserId())
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getOrdersByStatusAndUserId.status: Invalid status"))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn400_whenUserIdIsInvalid() {
        Long userId = 0L;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", OrderStatus.DELIVERED, userId)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: getOrdersByStatusAndUserId.userId: must be greater than " + userId))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn404_whenOrdersStatusNotFound() {
        OrderStatus orderStatus = OrderStatus.RETURNED;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given - Status not found
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", orderStatus, order1.getUserId())
                .then()
                .statusCode(404)
                .body("message", is("Order with status: " + orderStatus + " and user id: " + order1.getUserId() + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn404_whenUserIdNotFound() {
        Long userId = 10L;
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", OrderStatus.DELIVERED, userId)
                .then()
                .statusCode(404)
                .body("message", is("Order with status: " + OrderStatus.DELIVERED + " and user id: " + userId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void shouldUpdateOrderStatusById() {
        String expectedOrderStatus = "COMPLETED";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", order1.getId(), expectedOrderStatus)
                .then()
                .statusCode(200)
                .body("order.id", is(order1.getId().intValue()))
                .body("order.status", is(expectedOrderStatus))
                .body("order.paymentStatus", is(order1.getPaymentStatus().name()))
                .body("order.paymentMethod", is(order1.getPaymentMethod()))
                .body("order.shippingAddress", is(order1.getShippingAddress()))
                .body("order.shippingMethod", is(order1.getShippingMethod()))
                .body("order.orderDate", is(order1.getOrderDate().toString()))
                .body("order.deliveryDate", is(order1.getDeliveryDate().toString()));
    }

    @Test
    void updateOrderStatusById_shouldReturn400_whenOrderIdIsInvalid() {
        long orderId = 0L;
        String expectedOrderStatus = "COMPLETED";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", orderId, expectedOrderStatus)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: updateOrderStatusById.id: must be greater than " + orderId))
                .body("statusCode", is(400));
    }

    @Test
    void updateOrderStatusById_shouldReturn400_whenOrderStatusIsInvalid() {
        String expectedOrderStatus = "InvalidOrderStatus";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", order1.getId(), expectedOrderStatus)
                .then()
                .statusCode(400)
                .body("message", is("Validation error: updateOrderStatusById.status: Invalid status"))
                .body("statusCode", is(400));
    }

    @Test
    void updateOrderStatusById_shouldReturn404_whenOrderIdNotFound() {
        long orderId = 10L;
        String expectedOrderStatus = "COMPLETED";
        User user = userRepository.save(new User("username", "password", "email", "USER"));
        Order order1 = new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(5));
        Order order2 = new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDate.now(), LocalDate.now().plusDays(3));
        orderRepository.saveAll(List.of(order1, order2));

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", orderId, expectedOrderStatus)
                .then()
                .statusCode(404)
                .body("message", is("Order with id: " + orderId + " not found"))
                .body("statusCode", is(404));
    }
}