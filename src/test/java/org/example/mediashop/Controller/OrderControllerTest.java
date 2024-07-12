package org.example.mediashop.Controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.mediashop.Data.Entity.*;
import org.example.mediashop.Repository.OrderItemRepository;
import org.example.mediashop.Repository.OrderRepository;
import org.example.mediashop.Repository.ProductRepository;
import org.example.mediashop.Repository.UserRepository;
import org.example.mediashop.TestConfig.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class OrderControllerTest extends IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    User user;

    Product product;
    List<Order> orderList;

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
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(new User("username", "password", "email", "USER"));

        orderList = new ArrayList<>();
        orderList.add(new Order(user.getId(), 100.0, OrderStatus.DELIVERED, OrderPaymentStatus.COMPLETED, "Credit Card", "123 Main St, City, State, Zip", "Standard Shipping", LocalDateTime.parse("2024-07-10T00:00:00"), LocalDateTime.parse("2024-07-15T00:00:00")));
        orderList.add(new Order(user.getId(), 200.0, OrderStatus.CANCELLED, OrderPaymentStatus.RETURNED, "Credit Card", "456 Elm St, City, State, Zip", "Standard Shipping", LocalDateTime.parse("2024-07-10T00:00:00"), LocalDateTime.parse("2024-07-13T00:00:00")));
        orderRepository.saveAll(orderList);

        product = productRepository.save(new Product("ProductName1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "ShortDescription", "BrandName", "image1.jpg", 100.0, 10, null, null, null));
    }

    @Test
    void shouldGetOrderById() {
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/id/{0}", orderList.getFirst().getId().intValue())
                .then()
                .statusCode(200)
                .body("order.id", is(orderList.getFirst().getId().intValue()))
                .body("order.totalAmount", is(orderList.getFirst().getTotalAmount().floatValue()))
                .body("order.status", is(orderList.getFirst().getStatus().name()))
                .body("order.paymentStatus", is(orderList.getFirst().getPaymentStatus().name()))
                .body("order.paymentMethod", is(orderList.getFirst().getPaymentMethod()))
                .body("order.shippingAddress", is(orderList.getFirst().getShippingAddress()))
                .body("order.shippingMethod", is(orderList.getFirst().getShippingMethod()))
                .body("order.orderDate", is("2024-07-10T00:00:00"))
                .body("order.deliveryDate", is("2024-07-15T00:00:00"));
    }

    @Test
    void getOrderById_shouldReturn400_whenOrderIdIsInvalid() {
        long orderId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/id/{0}", orderId)
                .then()
                .statusCode(400)
                .body("message", is("getOrderById.id: must be greater than " + orderId))
                .body("statusCode", is(400));
    }

    @Test
    void getOrderById_shouldReturn404_whenOrderIdNotFound() {
        long orderId = 10L;

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
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}", OrderStatus.CANCELLED)
                .then()
                .statusCode(200)
                .body("order.", hasSize(1))
                .body("order[0].id", is(orderList.get(1).getId().intValue()))
                .body("order[0].totalAmount", is(orderList.get(1).getTotalAmount().floatValue()))
                .body("order[0].status", is(orderList.get(1).getStatus().name()))
                .body("order[0].paymentStatus", is(orderList.get(1).getPaymentStatus().name()))
                .body("order[0].paymentMethod", is(orderList.get(1).getPaymentMethod()))
                .body("order[0].shippingAddress", is(orderList.get(1).getShippingAddress()))
                .body("order[0].shippingMethod", is(orderList.get(1).getShippingMethod()))
                .body("order[0].orderDate", is("2024-07-10T00:00:00"))
                .body("order[0].deliveryDate", is("2024-07-13T00:00:00"));
    }

    @Test
    void getOrdersByStatus_shouldReturn400_whenOrderStatusIsInvalid() {
        String orderStatus = "InvalidOrderStatus";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}", orderStatus)
                .then()
                .statusCode(400)
                .body("message", is("getOrdersByStatus.status: must be any of enum class org.example.mediashop.Data.Entity.OrderStatus"))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatus_shouldReturn404_whenOrderStatusNotFound() {
        OrderStatus orderStatus = OrderStatus.EXPIRED;

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
        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", OrderStatus.DELIVERED, orderList.getFirst().getUserId())
                .then()
                .statusCode(200)
                .body("order.", hasSize(1))
                .body("order[0].id", is(orderList.getFirst().getId().intValue()))
                .body("order[0].totalAmount", is(orderList.getFirst().getTotalAmount().floatValue()))
                .body("order[0].status", is(orderList.getFirst().getStatus().name()))
                .body("order[0].paymentStatus", is(orderList.getFirst().getPaymentStatus().name()))
                .body("order[0].paymentMethod", is(orderList.getFirst().getPaymentMethod()))
                .body("order[0].shippingAddress", is(orderList.getFirst().getShippingAddress()))
                .body("order[0].shippingMethod", is(orderList.getFirst().getShippingMethod()))
                .body("order[0].orderDate", is("2024-07-10T00:00:00"))
                .body("order[0].deliveryDate", is("2024-07-15T00:00:00"));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn400_whenOrderStatusIsInvalid() {
        String orderStatus = "InvalidOrderStatus";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", orderStatus, orderList.getFirst().getUserId())
                .then()
                .statusCode(400)
                .body("message", is("getOrdersByStatusAndUserId.status: must be any of enum class org.example.mediashop.Data.Entity.OrderStatus"))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn400_whenUserIdIsInvalid() {
        Long userId = 0L;

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", OrderStatus.DELIVERED, userId)
                .then()
                .statusCode(400)
                .body("message", is("getOrdersByStatusAndUserId.userId: must be greater than " + userId))
                .body("statusCode", is(400));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn404_whenOrdersStatusNotFound() {
        OrderStatus orderStatus = OrderStatus.RETURNED;

        // Given - Status not found
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders/status/{0}/userId/{1}", orderStatus, orderList.getFirst().getUserId())
                .then()
                .statusCode(404)
                .body("message", is("Order with status: " + orderStatus + " and user id: " + orderList.getFirst().getUserId() + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void getOrdersByStatusAndUserId_shouldReturn404_whenUserIdNotFound() {
        Long userId = 10L;

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

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", orderList.getFirst().getId(), expectedOrderStatus)
                .then()
                .statusCode(200)
                .body("order.id", is(orderList.getFirst().getId().intValue()))
                .body("order.status", is(expectedOrderStatus))
                .body("order.paymentStatus", is(orderList.getFirst().getPaymentStatus().name()))
                .body("order.paymentMethod", is(orderList.getFirst().getPaymentMethod()))
                .body("order.shippingAddress", is(orderList.getFirst().getShippingAddress()))
                .body("order.shippingMethod", is(orderList.getFirst().getShippingMethod()))
                .body("order.orderDate", is("2024-07-10T00:00:00"))
                .body("order.deliveryDate", is("2024-07-15T00:00:00"));
    }

    @Test
    void updateOrderStatusById_shouldReturn400_whenOrderIdIsInvalid() {
        long orderId = 0L;
        String expectedOrderStatus = "COMPLETED";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", orderId, expectedOrderStatus)
                .then()
                .statusCode(400)
                .body("message", is("updateOrderStatusById.id: must be greater than " + orderId))
                .body("statusCode", is(400));
    }

    @Test
    void updateOrderStatusById_shouldReturn400_whenOrderStatusIsInvalid() {
        String expectedOrderStatus = "InvalidOrderStatus";

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/id/{0}/status/{1}", orderList.getFirst().getId(), expectedOrderStatus)
                .then()
                .statusCode(400)
                .body("message", is("updateOrderStatusById.status: must be any of enum class org.example.mediashop.Data.Entity.OrderStatus"))
                .body("statusCode", is(400));
    }

    @Test
    void updateOrderStatusById_shouldReturn404_whenOrderIdNotFound() {
        long orderId = 10L;
        String expectedOrderStatus = "COMPLETED";

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

    @Test
    void shouldCreateOrder() throws JsonProcessingException {
        Map<String, Object> order = new HashMap<>();
        order.put("userId", user.getId());
        order.put("status", OrderStatus.valueOf("COMPLETED"));
        order.put("paymentStatus", "PAID");
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("shippingAddress", "123 Main St, City, State, Zip Code");
        order.put("shippingMethod", "STANDARD");
        order.put("orderDate", "2024-07-10T00:00:00");
        order.put("deliveryDate", "2024-07-15T00:00:00");

        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productId", product.getId());
        productMap.put("discountId", 3);
        products.add(productMap);

        order.put("products", products);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(order);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/orders/new")
                .then()
                .statusCode(201)
                .body("order.id", is(notNullValue()))
                .body("order.userId", is(user.getId().intValue()))
                .body("order.totalAmount", is(product.getUnitPrice().floatValue()))
                .body("order.status", is("COMPLETED"))
                .body("order.paymentStatus", is("PAID"))
                .body("order.paymentMethod", is("CREDIT_CARD"))
                .body("order.shippingAddress", is("123 Main St, City, State, Zip Code"))
                .body("order.shippingMethod", is("STANDARD"))
                .body("order.orderDate", is("2024-07-10T00:00:00"))
                .body("order.deliveryDate", is("2024-07-15T00:00:00"))
                .body("order.products[0].productId", is(product.getId().intValue()))
                .body("order.products[0].discountId", is(3));
    }

    @Test
    void createOrder_shouldReturn404_whenUserIdNotFound() throws JsonProcessingException {
        long userId = 10L;

        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("status", OrderStatus.valueOf("COMPLETED"));
        order.put("paymentStatus", "PAID");
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("shippingAddress", "123 Main St, City, State, Zip Code");
        order.put("shippingMethod", "STANDARD");
        order.put("orderDate", "2024-07-10T00:00:00");
        order.put("deliveryDate", "2024-07-15T00:00:00");

        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productId", product.getId());
        productMap.put("discountId", 3);
        products.add(productMap);

        order.put("products", products);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(order);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/orders/new")
                .then()
                .statusCode(404)
                .body("message", is("User with id: " + userId + " not found"))
                .body("statusCode", is(404));
    }

    @Test
    void createOrder_shouldReturn400_whenUserIdIsInvalid() throws JsonProcessingException {
        long userId = 0L;

        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("status", OrderStatus.valueOf("COMPLETED"));
        order.put("paymentStatus", "PAID");
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("shippingAddress", "123 Main St, City, State, Zip Code");
        order.put("shippingMethod", "STANDARD");
        order.put("orderDate", "2024-07-10T00:00:00");
        order.put("deliveryDate", "2024-07-15T00:00:00");

        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productId", product.getId());
        productMap.put("discountId", 3);
        products.add(productMap);

        order.put("products", products);

        // Convert the Map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(order);

        // Given
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(jsonBody)
                .post("/api/v1/orders/new")
                .then()
                .statusCode(400)
                .body("message", is("UserId must be greater than zero"))
                .body("statusCode", is(400));
    }
}