package ru.practicum.test;

import io.qameta.allure.Step;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.ApiSpec;
import ru.practicum.Endpoints;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class ListOfOrdersTest {


    //В тело ответа возвращается список заказов
    @Test
    public void getListOfOrdersTest() {
        //1. Отправить запрос на получение списка заказов
        Response response = sendARequestToReceiveAListOfOrders();
        //2. Получить код ответа и убедиться в том, что список возвращается не пустой
        getTheResponseCodeAndTheListReturnedIsNotEmpty(response);
    }

    @Step("Send a request to receive a list of orders")
    public Response sendARequestToReceiveAListOfOrders() {
        Response response = given().spec(ApiSpec.getBaseSpec()).when().get(Endpoints.ORDER);
        return response;
    }

    @Step("Get the response code and the list returned is not empty")
    public void getTheResponseCodeAndTheListReturnedIsNotEmpty(Response response) {
        response.then().statusCode(200).body("orders", notNullValue());
    }
}