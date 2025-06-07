package ru.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ApiCourier {

    //Общие методы для тестовых классов Courier
    //Метод для шага "Отправить запрос на создание курьера"
    @Step("Send POST request to /api/v1/courier")
    public static Response sendPostRequestToCreateACourier(Courier courier) {
        Response creationResponse = given().spec(ApiSpec.getBaseSpec()).body(courier).when().post(Endpoints.COURIER);
        return creationResponse;
    }

    //Метод для шага "Отправить запрос логина курьера в системе"
    @Step("Send a request for a courier login in the system")
    public static Response sendARequestForACourierLoginInTheSystem(Courier courier) {
        Response responseLogin = given().spec(ApiSpec.getBaseSpec()).body(courier).when().post(Endpoints.COURIER_LOGIN);
        return responseLogin;
    }


    //Получить id курьера
    @Step("Extract courier ID from login response")
    public static int extractCourierIdFromResponse(Response loginResponse) {
        return loginResponse.then().extract().path("id");
    }


    //Удаление курьера из системы
    @Step("Courier delete")
    public static Response courierDelete(int courierId) {
        Response responseDelete = given().spec(ApiSpec.getBaseSpec()).when().delete(Endpoints.COURIER_DELETE +
                courierId);
        return responseDelete;
    }


    //Метод для шага "Проверить, что ответ возвращается с кодом 400 Bad Request"
    @Step("Verify that the response is returned with code 400 Bad Request")
    public static void theResponseIsReturnedWithCodeBadRequest(Response response) {
        response.then().statusCode(400);
    }


    //Метод для шага "Вывести тело ответа на экран"
    @Step("Print the response body to the screen")
    public static void printTheResponseBodyToTheScreen(Response response) {
        System.out.println(response.body().asString());
    }


    //CourierLoginTest
    //Метод для шага "Проверить, что возвращается код 200"
    @Step("Check that the code returned is 200")
    public static void checkThatTheCodeReturnedIs200(Response responseLogin) {
        responseLogin.then().statusCode(200);
    }


    //Метод для шага "Отправить запрос без логина"
    @Step("Send request without login")
    public static Response sendRequestWithoutLogin() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec()).body(new Courier(null, "1234")).when()
                .post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Отправить запрос без пароля"
    @Step("Send request without password")
    public static Response sendRequestWithoutPassword() {
        Response response = given().spec(ApiSpec.getBaseSpec()).body(new Courier("spider-man", null))
                .when().post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Проверить текст ошибки для запроса без логина или пароля"
    @Step("Check error text")
    public static void checkErrorText(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    //Метод для шага "Отправить запрос с некорректным логином"
    @Step("Send request with incorrect login")
    public static Response sendRequestWithIncorrectLogin() {
        Response response = given().spec(ApiSpec.getBaseSpec()).body(new Courier("piter-parker", "1234"))
                .when().post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Отправить запрос с некорректным паролем"
    @Step("Send request with incorrect password")
    public static Response sendRequestWithIncorrectPassword() {
        Response response = given().spec(ApiSpec.getBaseSpec()).body(new Courier("spider-man", "4321"))
                .when().post(Endpoints.COURIER_LOGIN);
        return response;
    }


    //Метод для шага "Проверить код ответа для запроса с несуществующей парой логин-пароль"
    @Step("Check response code for request with non-existent login-password pair")
    public static void checkResponseCodeForRequestWithNonExistentLoginPasswordPair(Response response) {
        response.then().statusCode(404);
    }


    //Метод для шага "Проверить текст ошибки для запроса с несуществующей парой логин-пароль"
    @Step("Check the error text for a request with a non-existent login-password pair")
    public static void checkTheErrorTextForARequestWithANonExistentLoginPasswordPair(Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }


    //CreatingACourierTest
    //Метод для шага "Проверить, что ответ возвращается с кодом 201 Created"
    @Step("Check that the response is returned with code 201 Created")
    public static void theResponseIsReturnedWithCode201Created(Response response) {
        response.then().statusCode(201);
    }


    //Метод для шага "Проверить сообщение в теле успешного запроса"
    @Step("Checking the message in the response body")
    public static void checkTheMessageInTheResponseBody(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }


    //Метод для шага "Проверить сообщение в теле ответа на запрос без одного из обязательных полей"
    @Step("Check the message in the request body without login or password")
    public static void checkTheMessageInTheRequestBodyWithoutLoginOrPassword(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной " +
                "записи"));
    }


    //Метод для шага "Проверить, что ответ возвращается с кодом 409 Conflict"
    @Step("Verify that the response is returned with code 409 Conflict")
    public static void theResponseIsReturnedWithCode409Conflict(Response response) {
        response.then().statusCode(409);
    }


    //Метод для шага "Проверить сообщение в теле ответа на запрос с повторяющимся логином"
    @Step("Check the message in the response body for a request with a duplicate login")
    public static void checkTheMessageInTheResponseBodyForARequestWithADuplicateLogin(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }
}