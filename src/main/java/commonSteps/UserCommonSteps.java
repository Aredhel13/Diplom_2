package commonSteps;

import models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.Url.*;
import static io.restassured.RestAssured.given;

public class UserCommonSteps {

    @Step("Регистрация пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User userDataForLogin) {
        return  given()
                .header("Content-type", "application/json")
                .body(userDataForLogin)
                .when()
                .post(LOGIN);
    }

    @Step("Изменение данных пользователя")
    public Response updateData(User userData, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .patch(USER);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token){
        given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

}
