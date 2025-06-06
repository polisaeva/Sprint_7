package ru.practicum.test;

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
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .when()
                .get(Endpoints.ORDER);
        //2. Получить код ответа и убедить в том, что список возвращается не пустой
        response.then().statusCode(200).body("orders", notNullValue());
    }
}