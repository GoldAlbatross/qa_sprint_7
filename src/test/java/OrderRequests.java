import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.Order;

import static io.restassured.RestAssured.given;

public class OrderRequests {

    public Response createOrder(Order order, String post) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(post);
    }

    public Response getOrdersList(String post) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(post);
    }
}
