import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class TestGetOrderList extends EzScooterApi {

    private final String ordersEndpoint = "/api/v1/orders";
    private final EzScooterRequests requests = new EzScooterRequests();

    @Test
    public void verifyGetOrdersList() {
        Response orderList = requests.getOrdersList(ordersEndpoint);
        orderList.then().statusCode(200);
       orderList.then().body("orders", is(not(empty())));
       orderList.then().body("orders", is(instanceOf(java.util.List.class)));
    }
}
