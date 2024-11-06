import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCourierLogin {

    private int id;
    private String login = "uniqueCourier2";
    private String password = "password123";
    private String firstName = "John";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void verifyCourierAuthorization() {
        createCourier();
        courierAuthorization();
        deleteCourier();
    }
    @Test
    public void verifyIncorrectLogin() {
        createCourier();
        incorrectLogin();
        getId();
        deleteCourier();
    }
    @Test
    public void verifyAuthorizationWithoutLogin() {
        createCourier();
        authorizationWithoutLogin();
        getId();
        deleteCourier();
    }
    @Step("Создаем курьера")
    private void createCourier() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\" }")
                .when()
                .post("/api/v1/courier");
    }
    @Step("Удаляем курьера по id")
    private void deleteCourier() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"id\": \"" + id + "\" }")
                .when()
                .delete("/api/v1/courier/" + id);
    }
    @Step("Получаем id для удаления курьера")
    private void getId() {
        id = given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");
    }
    @Step("Запрос на авторизацию")
    public void courierAuthorization() {
        id = given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }
    @Step("Запрос на авторизацию с не верным логином")
    public void incorrectLogin() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"login\": \"" + (login + "1") + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Step("Запрос на авторизацию без логина")
    public void authorizationWithoutLogin() {
        given()
                .header("Content-Type", "application/json")
                .body("{ \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
