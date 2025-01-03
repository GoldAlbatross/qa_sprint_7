import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.example.PostsForOrder.postOrder;
import static org.hamcrest.Matchers.*;

public class TestGetOrderList {

    private final OrderRequests requests = new OrderRequests();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void verifyGetOrdersList() {
        Response orderList = requests.getOrdersList(postOrder);
        orderList.then().statusCode(200);
       orderList.then().body("orders", is(not(empty())));
       orderList.then().body("orders", is(instanceOf(java.util.List.class)));
    }
}
