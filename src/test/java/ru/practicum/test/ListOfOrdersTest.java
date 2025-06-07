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
import static ru.practicum.ApiOrder.getTheResponseCodeAndTheListReturnedIsNotEmpty;
import static ru.practicum.ApiOrder.sendARequestToReceiveAListOfOrders;

public class ListOfOrdersTest {


    //В тело ответа возвращается список заказов
    @Test
    public void getListOfOrdersTest() {
        //1. Отправить запрос на получение списка заказов
        Response response = sendARequestToReceiveAListOfOrders();
        //2. Получить код ответа и убедиться в том, что список возвращается не пустой
        getTheResponseCodeAndTheListReturnedIsNotEmpty(response);
    }
}