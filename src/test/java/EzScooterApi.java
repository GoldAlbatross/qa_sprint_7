import io.restassured.RestAssured;
import org.junit.Before;

abstract class EzScooterApi {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}