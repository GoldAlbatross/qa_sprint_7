import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateCourier {

    private int id;
    private String login = "uniqueCourier2";
    private String password = "password123";
    private String firstName = "John";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void verifyCreateUniqueCourier() {
        createUniqueCourier();
        getId();
        deleteCourier();
    }
    @Test
    public void verifyCreateIdenticalCouriers() {
        createIdenticalCouriers();
        getId();
        deleteCourier();
    }
    @Test
    public void verifyCreateCourierWithoutLogin() {
        createCourierWithoutLogin();
    }

    @Step("Удаление курьера по id")
    private void deleteCourier() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"id\": \"" + id + "\" }")
                .when()
                .delete("/api/v1/courier/" + id);
    }
    @Step("Получение id для удаления")
    private void getId() {
        id = given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");
    }

    @Step("Запрос на создание курьера")
    public void createUniqueCourier() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\" }")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }
    @Step("Создание одинаковых курьеров")
    public void createIdenticalCouriers() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\" }")
                .when()
                .post("/api/v1/courier");

        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\" }")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Step("Запрос на создание курьера без поля пароль")
    public void createCourierWithoutLogin() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"firstName\": \"" + firstName + "\" }")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400) // Проверяем код ответа 400 Bad Request
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); // Проверяем сообщение об ошибке
    }
}
