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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

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
    @Test
    @DisplayName("Courier Can Be Created")
    @Description("The test checks the creation of the courier")
    public void CourierCanBeCreated() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier();
        //2. Проверить, что ответ возвращает с кодом 201 Created
        theResponseIsReturnedWithCode201Created(response);
        //3. Вывести тело ответа на экран
        printTheResponseBodyToTheScreen(response);
    }


    //Нельзя создать двух одинаковых курьеров
    //Запрос возвращает правильный код ответа
    @Test
    @DisplayName("Duplicate courier entries are not permitted")
    @Description("When creating a request with a duplicate username, the message \"This username is already in use\" is " +
            "returned")
    public void duplicateCourierEntriesAreNotPermitted() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier();
        //2. Проверить, что ответ возвращается с кодом 201 Created
        theResponseIsReturnedWithCode201Created(response);
        //3. Отправить повторный запрос на создание курьера с теми же параметрами
        Response duplicateResponse = sendPostRequestToCreateACourier();
        //4. Проверить, что ответ возвращается с кодом 409 Conflict
        theResponseIsReturnedWithCode409Conflict(duplicateResponse);
    }


    //Чтобы создать курьера, нужно передать в ручку все обязательные поля
    //Запрос возвращает правильный код ответа
    @Test
    @DisplayName("Сourier is not created without login")
    @Description("if you pass an empty value instead of the login field, the courier will not be created")
    public void courierIsNotCreatedWithoutLogin() {
        //1. Создать объект класса Courier со значением поля Логин null
        courier = new Courier(null, "1234");
        //2. Отправить запрос на создание курьера с пустым полем Логин
        checkMissingFieldFails(courier);
    }


    //Чтобы создать курьера, нужно передать в ручку все обязательные поля
    //Запрос возвращает правильный код ответа
    @Test
    @DisplayName("Сourier is not created without password")
    @Description("if you pass an empty value instead of the password field, the courier will not be created")
    public void courierIsNotCreatedWithoutPassword() {
        //1. Создать объект класса Courier со значением поля Пароль null
        courier = new Courier("spider-man", null);
        //2. Отправить запрос на создание курьера с пустым полем Пароль
        checkMissingFieldFails(courier);
    }


    //Успешный запрос возвращает в теле ответа ok: true
    @Test
    @DisplayName("A successful request returns ok: true in the response body")
    @Description("When all required fields are passed in the request to create a courier, a response with the body " +
            "ok: true is returned")
    public void successfulRequestReturnsOK() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier();
        //2. Проверить сообщение в теле успешного запроса
        checkTheMessageInTheResponseBody(response);
    }


    //Если одного из полей нет, запрос возвращает ошибку
    @Test
    @DisplayName("Error if login is not passed")
    @Description("If one of the fields is missing, the request returns the error \"Not enough data to create an " +
            "account\"")
    public void errorIfLoginIsNotPassed() {
        //1. Создать объект класса Courier со значением поля Логин null
        courier = new Courier(null, "1234");
        //2. Отправить запрос на создание курьера с пустым полем Логин
        Response response = checkMissingFieldFails(courier);
        //3. Проверить сообщение в теле ответа на запрос без одного из обязательных полей
        checkTheMessageInTheRequestBodyWithoutLoginOrPassword(response);
    }

    //Если одного из полей нет, запрос возвращает ошибку
    @Test
    @DisplayName("Error if password not passed")
    @Description("If one of the fields is missing, the request returns the error \"Not enough data to create an " +
            "account\"")
    public void errorIfPasswordNotPassed() {
        //1. Создать объект класса Courier со значением поля Пароль null
        courier = new Courier("spider-man", null);
        //2. Отправить запрос на создание курьера с пустым полем Пароль
        Response response = checkMissingFieldFails(courier);
        //3. Проверить сообщение в теле ответа на запрос без одного из обязательных полей
        checkTheMessageInTheRequestBodyWithoutLoginOrPassword(response);
    }

    //Если создать пользователя с логином, который уже есть, возвращается ошибка
    @Test
    @DisplayName("Error when creating identical couriers")
    @Description("If you create a user with a login that already exists, the error \"This login is already in use\" " +
            "is returned\"")
    public void errorWhenCreatingIdenticalCouriers() {
        //1. Отправить запрос на создание курьера
        Response response = sendPostRequestToCreateACourier();
        //2. Проверить, что ответ возвращается с кодом 201 Created
        theResponseIsReturnedWithCode201Created(response);
        //3. Отправить повторный запрос на создание курьера с теми же параметрами
        Response duplicateResponse = sendPostRequestToCreateACourier();
        //4. Проверить сообщение об ошибке в теле ответа
        checkTheMessageInTheResponseBodyForARequestWithADuplicateLogin(duplicateResponse);
    }


    //Метод для шага "Отправить запрос на создание курьера"
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestToCreateACourier() {
        Response response = given().spec(ApiSpec.getBaseSpec()).and().body(courier).when()
                .post("/api/v1/courier");
        return response;
    }


    //Метод для шага "Проверить, что ответ возвращается с кодом 201 Created"
    @Step("Check that the response is returned with code 201 Created")
    public void theResponseIsReturnedWithCode201Created(Response response) {
        response.then().statusCode(201);
    }


    //Метод для шага "Вывести тело ответа на экран"
    @Step("Print the response body to the screen")
    public void printTheResponseBodyToTheScreen(Response response) {
        System.out.println(response.body().asString());
    }


    //Метод для шага "Проверить, что ответ возвращается с кодом 409 Conflict"
    @Step("Verify that the response is returned with code 409 Conflict")
    public void theResponseIsReturnedWithCode409Conflict(Response response) {
        response.then().statusCode(409);
    }


    //Метод для шага "Проверить, что ответ возвращается с кодом 400 Bad Request"
    @Step("Verify that the response is returned with code 400 Bad Request")
    public void theResponseIsReturnedWithCodeBadRequest(Response response) {
        response.then().statusCode(400);
    }


    //Метод для шага "Проверить, что создание курьера без обязательного поля логин или пароль возвращает ошибку"
    @Step("Check missing field fails")
    public Response checkMissingFieldFails(Courier courier) {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then().statusCode(400);
        return response;
    }


    //Метод для шага "Проверить сообщение в теле успешного запроса"
    @Step("Checking the message in the response body")
    public void checkTheMessageInTheResponseBody(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }


    //Метод для шага "Проверить сообщение в теле ответа на запрос без одного из обязательных полей"
    @Step("Check the message in the request body without login or password")
    public void checkTheMessageInTheRequestBodyWithoutLoginOrPassword(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной " +
                "записи"));
    }


    //Метод для шага "Проверить сообщение в теле ответа на запрос с повторяющимся логином"
    @Step("Check the message in the response body for a request with a duplicate login")
    public void checkTheMessageInTheResponseBodyForARequestWithADuplicateLogin(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }


    @After
    @Step("Get courier id using login and password and delete courier")
    //Приведение таблицы Couriers в исходное состояние
    //Запрос возвращает правильный код ответа
    public void cleanUp() {
        // Логин курьера в системе
        Response loginResponse = given()
                .spec(ApiSpec.getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        // Если код ответа 200, то получаем ID курьера и удаляем его из системы
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.then().extract().path("id");
            given()
                    .spec(ApiSpec.getBaseSpec())
                    .when()
                    .delete("/api/v1/courier/" + courierId)
                    .then()
                    .statusCode(200);
        } else {
            System.out.println("Курьер не найден, удаление не требуется");
        }
    }
}