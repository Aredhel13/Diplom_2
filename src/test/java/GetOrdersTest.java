import Client.OrderCommonSteps;
import Models.Order;
import Models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static Config.Url.URL;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class GetOrdersTest extends OrderCommonSteps {
    User user;
    User userData;
    Order order;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        //Данные для регистрации пользователя
        user = new User("Toshiro_10@yandex.ru", "qwerty999", "Хицугая");
        //Данные для авторизации пользователя
        userData = new User(user.getEmail(), user.getPassword());
        accessToken = createUser(user).then().extract().path("accessToken");
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
        createOrder(order, accessToken);
    }

    @Test
    @DisplayName("Проверяем, что неавторизованный пользователь не может получить список заказов")
    public void getOderListForNotAuthorizationUser() {
        getOrdersList()
                .then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Проверяем, что авторизованный пользователь может получить список заказов")
    public void getOderListForAuthorizationUser() {
        accessToken = loginUser(userData).then().extract().path("accessToken");
        getOrdersList(accessToken)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }



    @After
    public void deleteUser() {
        deleteUser(accessToken);
    }
}
