import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.example.Courier;
import org.example.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class EzScooterRequests {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации объекта", e);
        }
    }

    public Response createCourier(Courier courier, String post) {
        String body = toJson(courier);
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(post);
    }

    @Step("Получаем id для удаления курьера")
    private int getId(Courier courier) {
        String body = toJson(courier);
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
    }

    @Step("Удаляем курьера по id")
    public Response deleteCourier(Courier courier, String post) {
        int id = getId(courier);
        String body = String.format("{ \"id\": %d }", id);
         return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .delete(post + id);
    }

    @Step("Запрос на авторизацию")
    public Response courierAuthorization(Courier courier, String post) {
        String body = toJson(courier);
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login");

    }

    @Step("Запрос на авторизацию с не верным логином")
    public Response incorrectLogin(Courier courier, String post) {

        String body = toJson(courier);
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(post);
    }

    @Step("Запрос на авторизацию без логина")
    public Response authorizationWithoutLogin(Courier courier, String post) {
        String body = toJson(courier);
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login");

    }

    public Response createOrder(Order order, String post) {
        String body = toJson(order);
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(post);
    }

    public Response getOrdersList(String post) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(post);
    }

//    public void getOrdersList() {
//        given()
//                .header("Content-Type", "application/json")
//                .when()
//                .get("/api/v1/orders")
//                .then()
//                .statusCode(200)
//                .body("orders", is(not(empty())))
//                .body("orders", is(instanceOf(java.util.List.class)));
//    }
}
