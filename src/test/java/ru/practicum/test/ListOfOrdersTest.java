package ru.practicum.test;

import io.restassured.RestAssured;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class ListOfOrdersTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    //В тело ответа возвращается список заказов
    @Test
    public void getListOfOrdersTest() {
        //1. Отправить запрос на получение списка заказов
        Response response = given()
                .when()
                .get("/api/v1/orders");
        //2. Получить код ответа и убедить в том, что список возвращается не пустой
        response.then().statusCode(200).body("orders", notNullValue());
    }
}