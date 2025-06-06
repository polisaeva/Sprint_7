package ru.practicum.test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.ApiSpec;
import ru.practicum.Courier;
import ru.practicum.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

//Логин курьера
public class CourierLoginTest {

    private final String login = "spider-man";
    private final String password = "1234";
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {

        courier = new Courier(login, password);
        given()
                .spec(ApiSpec.getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.COURIER)
                .then()
                .statusCode(201);
    }


    //Курьер может авторизоваться
    //Для авторизации нужно передать все обязательные поля
    @Test
    @DisplayName("The courier can log in")
    @Description("Upon successful authorization, code 200 is returned")
    public void theCourierCanLogIn() {
        //1. Отправить запрос логина курьера в системе
        Response response = sendARequestForACourierLoginInTheSystem();
        //2. Проверить, что возвращается код 200
        checkThatTheCodeReturnedIs200(response);
        //3. Вывести тело ответа на экран
        printTheResponseBodyOnTheScreen(response);
        //4. Получить id курьера
        courierId = getCourierId(response);
    }

    //Успешный запрос возвращает id
    @Test
    @DisplayName("A successful request returns id")
    @Description("The body of the request for successful login returns ID")
    public void successfulRequestReturnsId(){
        //1. Отправить запрос логина курьера в системе
        Response response = sendARequestForACourierLoginInTheSystem();
        //2. Проверить наличие ключа id в теле ответа
        response.then().assertThat().body("id", notNullValue());
        //3. Получить id курьера
        courierId = getCourierId(response);
    }

    //Для авторизации нужно передать все обязательные поля
    //Запрос без логина
    @Test
    @DisplayName("Request without login")
    @Description("To authorize, you must submit all required fields")
    public void requestWithoutLoginReturnsCode404() {
        //1. Отправить запрос без логина
        Response response = sendRequestWithoutLogin();
        //2. Проверить код ошибки для запроса без логина
        checkErrorCode(response);
    }


    //Для авторизации нужно передать все обязательные поля
    //Запрос без пароля
    @Test
    @DisplayName("Request without password")
    @Description("To authorize, you must submit all required fields")
    public void requestWithoutPasswordReturnsCode404() {
        //1. Отправить запрос без пароля
        Response response = sendRequestWithoutPassword();
        //2. Проверить код ошибки для запроса без пароля
        checkErrorCode(response);
    }


    //Для авторизации нужно передать все обязательные поля
    //Проверка тела ответа при запросе без логина
    @Test
    @DisplayName("Request without login")
    @Description("To authorize, you must submit all required fields")
    public void messageAccountNotFoundIfRequestWithoutLogin() {
        //1. Отправить запрос без логина
        Response response = sendRequestWithoutLogin();
        //2. Проверить текст ошибки для запроса без логина
        checkErrorText(response);
    }


    //Для авторизации нужно передать все обязательные поля
    //Проверка тела ответа при запросе без пароля
    @Test
    @DisplayName("Request without password")
    @Description("To authorize, you must submit all required fields")
    public void messageAccountNotFoundIfRequestWithoutPassword() {
        //1. Отправить запрос без пароля
        Response response = sendRequestWithoutPassword();
        //2. Проверить текст ошибки для запроса без пароля
        checkErrorText(response);
    }

    //Система вернёт ошибку, если неправильно указать логин
    @Test
    @DisplayName("Code 404 when requesting with non-existent login")
    @Description("An error with code 404 will be returned if the login is specified incorrectly")
    public void code404WhenRequestingWithNonExistentLogin() {
        //1. Отправить запрос с несуществующим логином
        Response response = sendRequestWithIncorrectLogin();
        //2. Проверить код ответа для запроса с несуществующей парой логин-пароль
        checkResponseCodeForRequestWithNonExistentLoginPasswordPair(response);
    }

    //Система вернёт ошибку, если неправильно указать пароль
    @Test
    @DisplayName("Code 404 when requesting with non-existent password")
    @Description("An error with code 404 will be returned if the password is specified incorrectly")
    public void code404WhenRequestingWithNonExistentPassword() {
        //1. Отправить запрос с несуществующим паролем
        Response response = sendRequestWithIncorrectPassword();
        //2. Проверить код ответа для запроса с несуществующей парой логин-пароль
        checkResponseCodeForRequestWithNonExistentLoginPasswordPair(response);
    }

    //Система вернёт ошибку, если неправильно указать логин
    @Test
    @DisplayName("Error body when requesting with non-existent login")
    @Description("If the login is incorrect, an error will be returned with the message \"Account not found\"")
    public void errorBodyWhenRequestingWithNonExistentLogin() {
        //1. Отправить запрос с несуществующим логином
        Response response = sendRequestWithIncorrectLogin();
        //2. Проверить текст ошибки для запроса с несуществующей парой логин-пароль
        checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(response);
    }

    //Система вернёт ошибку, если неправильно указать пароль
    @Test
    @DisplayName("Error body when requesting with non-existent password")
    @Description("If the password is incorrect, an error will be returned with the message \"Account not found\"")
    public void errorBodyWhenRequestingWithNonExistentPassword() {
        //1. Отправить запрос с несуществующим паролем
        Response response = sendRequestWithIncorrectPassword();
        //2. Проверить текст ошибки для запроса с несуществующей парой логин-пароль
        checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(response);
    }


    //Метод для шага "Отправить запрос логина курьера в системе"
    @Step("Send a request for a courier login in the system")
    public Response sendARequestForACourierLoginInTheSystem() {
        Response responseLogin = given()
                .spec(ApiSpec.getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.COURIER_LOGIN);
        return responseLogin;
    }


    //Метод для шага "Проверить, что возвращается код 200"
    @Step("Check that the code returned is 200")
    public void checkThatTheCodeReturnedIs200(Response responseLogin) {
        responseLogin.then().statusCode(200);
    }


    //Метод для шага "Вывести тело ответа на экран"
    @Step("Print the response body on the screen")
    public void printTheResponseBodyOnTheScreen(Response responseLogin) {
        System.out.println(responseLogin.body().asString());
    }


    //Метод для шага "Получить id курьера"
    @Step("Get courier ID")
    public int getCourierId(Response response) {
        return response.then().extract().path("id");
    }


    //Метод для шага "Отправить запрос без логина"
    @Step("Send request without login")
    public Response sendRequestWithoutLogin() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .body(new Courier(null, "1234"))
                .when()
                .post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Отправить запрос без пароля"
    @Step("Send request without password")
    public Response sendRequestWithoutPassword() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .body(new Courier("spider-man", null))
                .when()
                .post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Проверить код ошибки для запроса без логина или пароля"
    @Step("Check error code")
    public void checkErrorCode(Response response) {
        response.then().statusCode(400);
    }


    //Метод для шага "Проверить текст ошибки для запроса без логина или пароля"
    @Step("Check error text")
    public void checkErrorText(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    //Метод для шага "Отправить запрос с некорректным логином"
    @Step("Send request with incorrect login")
    public Response sendRequestWithIncorrectLogin() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .body(new Courier("piter-parker", "1234"))
                .when()
                .post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Отправить запрос с некорректным паролем"
    @Step("Send request with incorrect password")
    public Response sendRequestWithIncorrectPassword() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .body(new Courier("spider-man", "4321"))
                .when()
                .post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Проверить код ответа для запроса с несуществующей парой логин-пароль"
    @Step("Check response code for request with non-existent login-password pair")
    public void checkResponseCodeForRequestWithNonExistentLoginPasswordPair(Response response) {
        response.then().statusCode(404);
    }


    //Метод для шага "Проверить текст ошибки для запроса с несуществующей парой логин-пароль"
    @Step("Check the error text for a request with a non-existent login-password pair")
    public void checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }


    @After
    //Приведение таблицы Couriers в исходное состояние
    //Запрос возвращает правильный код ответа
    public void cleanUp() {
        // Логин курьера в системе
        Response loginResponse = given()
                .spec(ApiSpec.getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.COURIER_LOGIN);

        // Если код ответа 200, то получаем ID курьера и удаляем его из системы
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.then().extract().path("id");
            given()
                    .spec(ApiSpec.getBaseSpec())
                    .when()
                    .delete(Endpoints.COURIER_DELETE + courierId)
                    .then()
                    .statusCode(200);
        } else {
            System.out.println("Курьер не найден, удаление не требуется");
        }
    }
}