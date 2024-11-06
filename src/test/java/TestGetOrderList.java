import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestGetOrderList {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void verifyGetOrdersList() {
        getOrdersList();
    }
    @Step("Запрос на получение списка заказов")
    public void getOrdersList() {
        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", is(not(empty())))
                .body("orders", is(instanceOf(java.util.List.class)));
    }
}
