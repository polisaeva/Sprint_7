package ru.practicum.test;


import io.qameta.allure.Step;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.ApiSpec;
import ru.practicum.Endpoints;
import ru.practicum.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


//Создание заказа
@RunWith(Parameterized.class)
public class CreateAnOrderTest {

    private Order order;
    private String nameTest;
    private List<String> colors;
    private int expectedStatusCode;
    private int getOrderNumber;

    public CreateAnOrderTest(List<String> colors, int expectedStatusCode, String nameTest) {
        this.colors = colors;
        this.expectedStatusCode = expectedStatusCode;
        this.nameTest = nameTest;
    }


    @Parameterized.Parameters(name = "Тест \"{2}\": Ожидаемый код ответа {1} при значении параметра color - {0}")
    public static Collection<Object[]> checkingResponseCodeForColorParameter() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK"), 201, "Можно указать BLACK"},
                {Arrays.asList("GREY"), 201, "Можно указать GREY"},
                {Arrays.asList("black"), 201, "Проверка зависимости от регистра. Можно указать black"},
                {Arrays.asList("grey"), 201, "Проверка зависимости от регистра. Можно указать grey"},
                {Arrays.asList("PINK"), 400, "Нельзя указать цвет, который не входит в список доступных"},
                {Arrays.asList("BLACK", "GREY"), 201, "Можно указать BLACK и GREY"},
                {Arrays.asList(), 201, "Пустой массив цветов"},
                {null, 201, "Параметр color = null"}
        });
    }

    @Before
    public void setUp() {
        order = new Order(
                "Gwen",
                "Stacy",
                "New York City, 65 apt.",
                2,
                "+7 999 654 75 57",
                3,
                "2025-06-15",
                "I have to go to England. It's important to me. I don't even know, maybe our paths are now " +
                        "diverging forever...",
                colors);
    }


    @Test
    public void creatingAnOrderTest() {
        //1. Отправить запрос на создание заказа
        Response response = sendARequestToCreateAnOrder();
        //2. Проверить код ответа
        response.then().statusCode(expectedStatusCode);
        //3. Проверить, что в теле ответа возвращается track и получить номер заказа
        if (expectedStatusCode == 201) {
            getOrderNumber = trackIsReturnedInTheResponseBody(response);
        }
    }

    //Метод для шага "Отправить запрос на создание заказа"
    @Step("Send a request to create an order")
    public Response sendARequestToCreateAnOrder() {
        Response response = given()
                .spec(ApiSpec.getBaseSpec())
                .body(order)
                .when()
                .post(Endpoints.ORDER);
        return response;
    }

    //Метод для шага "Проверить, что в теле ответа возвращается track и получить номер заказа"
    @Step("Track is returned in the response body")
    public int trackIsReturnedInTheResponseBody(Response response) {
        response.then().body("track", notNullValue());
        int orderNumber = response.jsonPath().getInt("track");
        return orderNumber;
    }


    @After
    //Удаление созданного заказа из системы
    public void cleanUp() {
        //Отменяем заказ
        if (expectedStatusCode == 201) {
            given()
                    .spec(ApiSpec.getBaseSpec())
                    .queryParam("track", getOrderNumber)
                    .when()
                    .put(Endpoints.CANSEL_ORDER)
                    .then()
                    .statusCode(200);
        }
    }
}