import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestCreateOrder extends EzScooterApi {

    private final EzScooterRequests requests = new EzScooterRequests();
    private final String postCreateOrder = "/api/v1/orders";
    private String[] colors;

    public TestCreateOrder(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}}
        };
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order(colors);
        Response createOrder = requests.createOrder(order, postCreateOrder);
        createOrder.then().statusCode(201);
        createOrder.then().body("track", notNullValue());
    }
}
