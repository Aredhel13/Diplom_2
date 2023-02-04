package commonSteps;

import models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.Url.ORDERS;
import static io.restassured.RestAssured.given;

public class OrderCommonSteps extends UserCommonSteps{

    @Step
    public Response createOrder(Order order, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step
    public  Response getOrdersList(String accessToken){
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .get(ORDERS);
    }

    @Step
    public  Response getOrdersList(){
        return given()
                .header("Content-type", "application/json")
                .get(ORDERS);
    }
}
