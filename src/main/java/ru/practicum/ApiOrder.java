package ru.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ApiOrder {


//CreateAnOrderTest
    //Метод для шага "Отправить запрос на создание заказа"
    @Step("Send a request to create an order")
    public static Response sendARequestToCreateAnOrder(Order order) {
        Response response = given().spec(ApiSpec.getBaseSpec()).body(order).when().post(Endpoints.ORDER);
        return response;
    }

    //Метод для шага "Проверить, что в теле ответа возвращается track и получить номер заказа"
    @Step("Track is returned in the response body")
    public static int trackIsReturnedInTheResponseBody(Response response) {
        response.then().body("track", notNullValue());
        int orderNumber = response.jsonPath().getInt("track");
        return orderNumber;
    }

    //Отмена заказа
    @Step("Cancel order")
    public static Response cancelOrder(int getOrderNumber) {
        Response responseCancel = given().spec(ApiSpec.getBaseSpec()).queryParam("track", getOrderNumber).when()
                .put(Endpoints.CANCEL_ORDER);
        return responseCancel;
    }


//ListOfOrdersTest
    //Отправить запрос на получение списка заказов
    @Step("Send a request to receive a list of orders")
    public static Response sendARequestToReceiveAListOfOrders() {
        Response response = given().spec(ApiSpec.getBaseSpec()).when().get(Endpoints.ORDER);
        return response;
    }

    @Step("Get the response code and the list returned is not empty")
    public static void getTheResponseCodeAndTheListReturnedIsNotEmpty(Response response) {
        response.then().statusCode(200).body("orders", notNullValue());
    }
}