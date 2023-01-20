import Client.UserCommonSteps;
import Models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static Config.Url.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest extends UserCommonSteps {
    User user;
    User userDataForLogin;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        //Данные для регистрации пользователя
        user = new User("Toshiro_14@yandex.ru", "qwerty999", "Хицугая");
        //Данные для авторизации пользователя
        userDataForLogin = new User(user.getEmail(), user.getPassword());
        //Регистрация пользователя
        given()
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER);
    }

    @Test
    @DisplayName("Проверяем, что можно залогиниться под существующим пользователем")
    public void checkSuccessfulLogin() {
        loginUser(userDataForLogin)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Проверяем, что нельзя залогиниться с несуществующим email")
    public void checkUnsuccessfulLoginWithIncorrectEmail() {
        userDataForLogin.setEmail("Toshiro");
        loginUser(userDataForLogin)
                .then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Проверяем, что нельзя залогиниться с неверным паролем")
    public void checkUnuccessfulLoginWithIncorrectPassword() {
        userDataForLogin.setPassword("Toshiro");
        loginUser(userDataForLogin)
                .then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }
}
