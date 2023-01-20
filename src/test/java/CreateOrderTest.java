import Client.OrderCommonSteps;
import Models.Order;
import Models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static Config.Url.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest extends OrderCommonSteps {
    User user;
    User userData;
    Order order;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        //Данные для регистрации пользователя
        user = new User("Toshiro_13@yandex.ru", "qwerty999", "Хицугая");
        //Данные для авторизации пользователя
        userData = new User(user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("Проверяем успешное создание заказа для авторизованного пользователя")
    public void checkSuccessCreateOrderForLoginUser(){
        accessToken = createUser(user).then().extract().path("accessToken");
        accessToken = loginUser(userData).then().extract().path("accessToken");
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
        createOrder(order,accessToken)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверяем успешное создание заказа для неавторизованного пользователя")
    public void checkSuccessCreateOrderForLogoutUser(){
        accessToken = "";
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
        createOrder(order,accessToken)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверяем неуспешное создание заказа при передаче некорректных хэш-кодов")
    public void checkCreateOrderWithIncorrectIngredientHashCode(){
        accessToken = createUser(user).then().extract().path("accessToken");
        accessToken = loginUser(userData).then().extract().path("accessToken");
        order = new Order(List.of("0","просто какой-то текст"));
        createOrder(order,accessToken)
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Проверяем неуспешное создание заказа при передаче пустого массива")
    public void checkCreateOrderWithEmptyIngredientList(){
        accessToken = createUser(user).then().extract().path("accessToken");
        accessToken = loginUser(userData).then().extract().path("accessToken");
        order = new Order(List.of());
        createOrder(order,accessToken)
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @After
    public void deleteUser() {
        deleteUser(accessToken);
    }
}
