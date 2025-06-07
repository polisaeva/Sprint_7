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
import static ru.practicum.ApiCourier.*;

//Создание курьера
public class CreatingACourierTest {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = new Courier("spider-man", "1234");
    }


    //Курьера можно создать
    //Запрос возвращает правильный код ответа
    //Успешный запрос возвращает в теле ответа ok: true
    @Test
    @DisplayName("Courier Can Be Created. A successful request returns ok: true in the response body")
    @Description("The test checks the creation of the courier. When all required fields are passed in the request to " +
            "create a courier, a response with the body ok: true is returned")
    public void CourierCanBeCreatedTest() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier(courier);
        //2. Проверить, что ответ возвращает с кодом 201 Created
        theResponseIsReturnedWithCode201Created(response);
        //3. Проверить сообщение в теле успешного запроса
        checkTheMessageInTheResponseBody(response);
        //4. Вывести тело ответа на экран
        printTheResponseBodyToTheScreen(response);
    }


    //Нельзя создать двух одинаковых курьеров
    //Запрос возвращает правильные код ответа и сообщение в теле
    @Test
    @DisplayName("Duplicate courier entries are not permitted")
    @Description("When creating a request with a duplicate username, the message \"This username is already in use\" is " +
            "returned")
    public void duplicateCourierEntriesAreNotPermittedTest() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier(courier);
        //2. Проверить, что ответ возвращается с кодом 201 Created
        theResponseIsReturnedWithCode201Created(response);
        //3. Отправить повторный запрос на создание курьера с теми же параметрами
        Response duplicateResponse = sendPostRequestToCreateACourier(courier);
        //4. Проверить, что ответ возвращается с кодом 409 Conflict
        theResponseIsReturnedWithCode409Conflict(duplicateResponse);
        //5. Проверить сообщение в теле ответа на запрос с повторяющимся логином
        checkTheMessageInTheResponseBodyForARequestWithADuplicateLogin(duplicateResponse);
    }


    //Чтобы создать курьера, нужно передать в ручку все обязательные поля
    //Запрос возвращает правильные код ответа и сообщение в теле
    @Test
    @DisplayName("Сourier is not created without login")
    @Description("if you pass an empty value instead of the login field, the courier will not be created")
    public void courierIsNotCreatedWithoutLoginTest() {
        //1. Создать объект класса Courier со значением поля Логин null
        courier = new Courier(null, "1234");
        //2. Отправить запрос на создание курьера с пустым полем Логин
        Response response = sendPostRequestToCreateACourier(courier);
        //3. Проверить, что ответ возвращается с кодом 400 Bad Request
        theResponseIsReturnedWithCodeBadRequest(response);
        //3. Проверить сообщение в теле ответа на запрос без одного из обязательных полей
        checkTheMessageInTheRequestBodyWithoutLoginOrPassword(response);
    }


    //Чтобы создать курьера, нужно передать в ручку все обязательные поля
    //Запрос возвращает правильные код ответа и сообщение в теле
    @Test
    @DisplayName("Сourier is not created without password")
    @Description("if you pass an empty value instead of the password field, the courier will not be created")
    public void courierIsNotCreatedWithoutPasswordTest() {
        //1. Создать объект класса Courier со значением поля Пароль null
        courier = new Courier("spider-man", null);
        //2. Отправить запрос на создание курьера с пустым полем Пароль
        Response response = sendPostRequestToCreateACourier(courier);
        //3. Проверить, что ответ возвращается с кодом 400 Bad Request
        theResponseIsReturnedWithCodeBadRequest(response);
        //3. Проверить сообщение в теле ответа на запрос без одного из обязательных полей
        checkTheMessageInTheRequestBodyWithoutLoginOrPassword(response);
    }


    @After
    @Step("Get courier id using login and password and delete courier")
    //Приведение таблицы Couriers в исходное состояние
    //Запрос возвращает правильный код ответа
    public void cleanUp() {
        // Отправить запрос логина курьера в системе
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