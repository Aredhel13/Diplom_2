import Client.UserCommonSteps;
import Models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static Config.Url.*;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserDataTest extends UserCommonSteps {
    User user;
    User userData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        //Данные для регистрации пользователя
        user = new User("Toshiro_13@yandex.ru", "qwerty999", "Хицугая");
        //Данные для авторизации пользователя
        userData = new User(user.getEmail(), user.getPassword());
        //Регистрация пользователя
        accessToken = createUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем, что авторизованный пользователь может изменить email")
    public void checkUpdateEmailForLoginUser(){
        accessToken = loginUser(userData).then().extract().path("accessToken");
        userData.setEmail("new_email@yandex.ru");
        updateData(userData, accessToken)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверяем, что авторизованный пользователь может изменить имя")
    public void checkUpdateNameForLoginUser(){
        accessToken = loginUser(userData).then().extract().path("accessToken");
        userData.setName("new_name");
        updateData(userData, accessToken)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверяем, что авторизованный пользователь не может изменить email на существующий в системе")
    public void checkUnsuccessfulUpdateDataForLoginUser(){
        String accessTokenForDeleteFirstUser = accessToken; //токен первого пользователя
        String email = user.getEmail(); //запоминаем email 1 пользователя
        user.setEmail("Toshiro_133@yandex.ru"); //устанавливаем новый email для создания 2 пользователя
        accessToken = createUser(user).then().extract().path("accessToken"); //создаём второго пользователя с другим email
        userData.setName(email); //email для изменения = email 1 пользователя
        updateData(userData, accessToken)
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User with such email already exists"));
        //удаляем созданного первым пользователя
        deleteUser(accessTokenForDeleteFirstUser);
    }

    @Test
    @DisplayName("Проверяем, что неавторизованный пользователь не может изменить email")
    public void checkUnsuccessfulUpdateEmailForNotAuthorizedUser(){
        accessToken = "";
        userData.setEmail("new_email@yandex.ru");
        updateData(userData, accessToken)
                .then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Проверяем, что неавторизованный пользователь не может изменить имя")
    public void checkUnsuccessfulUpdateNameForNotAuthorizedUser(){
        accessToken = "";
        userData.setName("new_name");
        updateData(userData, accessToken)
                .then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {deleteUser(accessToken);}
    }
}
