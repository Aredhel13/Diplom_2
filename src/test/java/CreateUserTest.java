import Models.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    User user;
    String userRegisterLink = "/api/auth/register";
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = new User("Toshiro_14@yandex.ru", "qwerty999", "Хицугая");
    }

    @Step("Создаём пользователя")
    public Response createUser() {
        Response userPostResult = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(userRegisterLink);
        accessToken = userPostResult.then().extract().path("accessToken");
        return userPostResult;
    }



    @Test
    @DisplayName("Проверяем код и тело ответа при успешном создании пользователя")
    public void checkStatusAndBodySuccessCreate() {
        createUser()
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать двух одинаковых курьеров")
    public void checkStatusCodeWhenDuplicateUserCreate() {
        createUser();
        createUser()
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверяем, что необходимо заполнять обязательное поле: email")
    public void checkStatusCodeCreateWithoutLogin() {
        user.setEmail("");
        createUser()
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
        createUser()
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
        createUser()
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
        createUser()
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
        createUser()
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
        createUser()
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
            Response a = given()
                    .header("Authorization", accessToken)
                    .header("Content-type", "application/json")
                    .and()
                    .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
        System.out.println(a.getStatusCode());
        System.out.println(a.getBody().print());
        System.out.println(accessToken);
        }
    }
}
