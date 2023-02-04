import commonSteps.UserCommonSteps;
import models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static config.Url.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends UserCommonSteps {
    User user;
    String accessToken = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        String email = RandomStringUtils.randomAlphanumeric(10);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphabetic(20);
        user = new User(email +"@yandex.ru", password, name);
    }


    @Test
    @DisplayName("Проверяем код и тело ответа при успешном создании пользователя")
    public void checkStatusAndBodySuccessCreate() {
        Response createUserResponse = createUser(user);
        createUserResponse
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = createUserResponse.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать двух одинаковых пользователей")
    public void checkStatusCodeWhenDuplicateUserCreate() {
        String accessToken1 = createUser(user).then().extract().path("accessToken");
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
        //получаем токен, чтобы потом удалить пользователя
        accessToken = accessToken1;
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: email")
    public void checkStatusCodeCreateWithoutLogin() {
        user.setEmail("");
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: password")
    public void checkStatusCodeCreateWithoutPassword() {
        user.setEmail("");
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: name")
    public void checkStatusCodeCreateWithoutName() {
        user.setName("");
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: email")
    public void checkStatusCodeCreateWithNullLogin() {
        user.setEmail(null);
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: password")
    public void checkStatusCodeCreateWithNullPassword() {
        user.setPassword(null);
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: name")
    public void checkStatusCodeCreateWithNullName() {
        user.setName(null);
        createUser(user)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }
}
