import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCourierLogin extends EzScooterApi {

    private int id;
    private final String login = "courierUniqueLogin";
    private final String password = "password123";
    private final String firstName = "John";
    private final EzScooterRequests requests = new EzScooterRequests();
    private final String postCreateCourier = "/api/v1/courier";
    private final String postCourierAutorization = "/api/v1/courier/login";
    private final String postDeleteCourier = "/api/v1/courier/";
    private Courier courier;

    @After
    public void tearDown() {
        requests.deleteCourier(courier, postDeleteCourier);
    }

    @Test
    public void verifyCourierIncorrectLogin() {
        courier = new Courier(login, password, firstName);
        requests.createCourier(courier, postCreateCourier);
        Courier courierWithWrongLogin = new Courier("123", password, firstName);
        Response incorrectLogin = requests.incorrectLogin(courierWithWrongLogin, postCourierAutorization);
        incorrectLogin.then().statusCode(404);
        incorrectLogin.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void verifyCorrectAuthorization() {
        courier = new Courier(login, password, firstName);
        requests.createCourier(courier, postCreateCourier);
        Response correctAuthorization = requests.courierAuthorization(courier, postCourierAutorization);
        correctAuthorization.then().statusCode(200);
        correctAuthorization.then().body("id", notNullValue());

    }

    @Test
    public void verifyAuthorizationWithoutLogin() {
        courier = new Courier(login, password, firstName);
        requests.createCourier(courier, postCreateCourier);
        Courier courierWithOutLogin = new Courier(null, password, firstName);
        Response authorizationWithoutLogin = requests.authorizationWithoutLogin(courierWithOutLogin, postCourierAutorization);
        authorizationWithoutLogin.then().statusCode(400);
        authorizationWithoutLogin.then().body("message", equalTo("Недостаточно данных для входа"));
    }
}
