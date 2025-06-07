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
import static ru.practicum.ApiCourier.*;

//Логин курьера
public class CourierLoginTest {

    private final String login = "spider-man";
    private final String password = "1234";
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {

        courier = new Courier(login, password);
        Response creationResponse = sendPostRequestToCreateACourier(courier);
        creationResponse.then().statusCode(201);
    }


    //Курьер может авторизоваться
    //Для авторизации нужно передать все обязательные поля
    //Успешный запрос возвращает id
    @Test
    @DisplayName("The courier can log in. The body of the request for successful login returns ID")
    @Description("Upon successful authorization, code 200 is returned")
    public void theCourierCanLogInTest() {
        //1. Отправить запрос логина курьера в системе
        Response response = sendARequestForACourierLoginInTheSystem(courier);
        //2. Проверить, что возвращается код 200
        checkThatTheCodeReturnedIs200(response);
        //3. Проверить наличие ключа id в теле ответа
        response.then().assertThat().body("id", notNullValue());
        //4. Вывести тело ответа на экран
        printTheResponseBodyToTheScreen(response);
    }


    //Для авторизации нужно передать все обязательные поля
    //Запрос без логина
    //Проверка тела ответа при запросе без логина
    @Test
    @DisplayName("Request without login")
    @Description("To authorize, you must submit all required fields")
    public void requestWithoutLoginReturnsCode404Test() {
        //1. Отправить запрос без логина
        Response response = sendRequestWithoutLogin();
        //2. Проверить, что ответ возвращается с кодом 400 Bad Request
        theResponseIsReturnedWithCodeBadRequest(response);
        //3. Проверить текст ошибки для запроса без логина
        checkErrorText(response);
    }


    //Для авторизации нужно передать все обязательные поля
    //Запрос без пароля
    //Проверка тела ответа при запросе без пароля
    @Test
    @DisplayName("Request without password")
    @Description("To authorize, you must submit all required fields")
    public void requestWithoutPasswordReturnsCode404Test() {
        //1. Отправить запрос без пароля
        Response response = sendRequestWithoutPassword();
        //2. Проверить код ошибки для запроса без пароля
        theResponseIsReturnedWithCodeBadRequest(response);
        //3. Проверить текст ошибки для запроса без пароля
        checkErrorText(response);
    }


    //Система вернёт ошибку, если неправильно указать логин
    @Test
    @DisplayName("Code 404 when requesting with non-existent login. Error body when requesting with non-existent login")
    @Description("An error with code 404 will be returned if the login is specified incorrectly. If the login is " +
            "incorrect, an error will be returned with the message \"Account not found\"")
    public void code404WhenRequestingWithNonExistentLoginTest() {
        //1. Отправить запрос с несуществующим логином
        Response response = sendRequestWithIncorrectLogin();
        //2. Проверить код ответа для запроса с несуществующей парой логин-пароль
        checkResponseCodeForRequestWithNonExistentLoginPasswordPair(response);
        //3. Проверить текст ошибки для запроса с несуществующей парой логин-пароль
        checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(response);
    }

    //Система вернёт ошибку, если неправильно указать пароль
    @Test
    @DisplayName("Code 404 when requesting with non-existent password. Error body when requesting with non-existent " +
            "password")
    @Description("An error with code 404 will be returned if the password is specified incorrectly. If the password " +
            "is incorrect, an error will be returned with the message \"Account not found\"")
    public void code404WhenRequestingWithNonExistentPasswordTest() {
        //1. Отправить запрос с несуществующим паролем
        Response response = sendRequestWithIncorrectPassword();
        //2. Проверить код ответа для запроса с несуществующей парой логин-пароль
        checkResponseCodeForRequestWithNonExistentLoginPasswordPair(response);
        //3. Проверить текст ошибки для запроса с несуществующей парой логин-пароль
        checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(response);
    }


    @After
    //Приведение таблицы Couriers в исходное состояние
    //Запрос возвращает правильный код ответа
    public void cleanUp() {
        // Логин курьера в системе
        Response loginResponse = sendARequestForACourierLoginInTheSystem(courier);

        // Если код ответа 200, то получаем ID курьера и удаляем его из системы
        if (loginResponse.statusCode() == 200) {
            courierId = extractCourierIdFromResponse(loginResponse);
            Response responseDelete = courierDelete(courierId);
            responseDelete.then().statusCode(200);
        } else {
            System.out.println("Курьер не найден, удаление не требуется");
        }
    }
}