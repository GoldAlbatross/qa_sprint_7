import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestCreateOrder {

    private String colorBlack = "BLACK";
    private String colorGrey = "GREY";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void verifyCreateOrderWithOneColor() {
        createOrderWithOneColor();
    }
    @Test
    public void verifyCreateOrderWithoutColor() {
        createOrderWithoutColor();
    }
    @Test
    public void verifyCreateOrderWithBothColors() {
        createOrderWithBothColors();
    }
    @Step("Запрос на создание заказа с одним цветом")
    public void createOrderWithOneColor() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"color\": [\"" + colorBlack + "\"] }")
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
    @Step("Запрос на создание заказа с обоими цветами")
    public void createOrderWithBothColors() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"color\": [\"" + colorBlack + "\", \"" + colorGrey + "\"] }")
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
    @Step("Запрос на создание заказа без цвета")
    public void createOrderWithoutColor() {
        given()
                .header("Content-Type", "application/json")
                .body("{}") // Тело без указания цвета
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
