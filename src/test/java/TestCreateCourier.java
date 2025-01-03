import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.proxy;
import static org.example.PostsForCourier.postCreateCourier;
import static org.example.PostsForCourier.postDeleteCourier;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateCourier {

    private int id;
    private final String login = "courierUniqueLogin";
    private final String password = "password123";
    private final String firstName = "John";
    private final CourierRequests requests = new CourierRequests();
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void tearDown() {
        requests.deleteCourier(courier, postDeleteCourier);
    }

    @Test
    public void verifyCreateUniqueCourier() {
        courier = new Courier(login, password, firstName);
        Response createCourier = requests.createCourier(courier, postCreateCourier);
        createCourier.then().statusCode(201);
        createCourier.then().body("ok", equalTo(true));
    }
    @Test
    public void verifyCreateIdenticalCouriers() {
        courier = new Courier(login, password, firstName);
        requests.createCourier(courier, postCreateCourier);
        Response createIdenticalCourier = requests.createCourier(courier, postCreateCourier);
        createIdenticalCourier.then().statusCode(409);
        createIdenticalCourier.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Test
    public void verifyCreateCourierWithoutLogin() {
        courier = new Courier(null, password, firstName);
        Response createCourier = requests.createCourier(courier, postCreateCourier);
        createCourier.then().statusCode(400);
        createCourier.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
