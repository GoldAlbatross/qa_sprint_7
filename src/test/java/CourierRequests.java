import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;

import static io.restassured.RestAssured.given;

public class CourierRequests {

    public Response createCourier(Courier courier, String post) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(post);
    }

    @Step("Получаем id для удаления курьера")
    private int getId(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
    }

    @Step("Удаляем курьера по id")
    public Response deleteCourier(Courier courier, String post) {
        if (courier.isFieldInitialized()) {
            int id = getId(courier);
            String body = String.format("{ \"id\": %d }", id);
            return RestAssured.given()
                    .header("Content-Type", "application/json")
                    .body(body)
                    .when()
                    .delete(post + id);
        } else {
            return null;
        }
    }

    @Step("Запрос на авторизацию")
    public Response courierAuthorization(Courier courier, String post) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

    }
}
