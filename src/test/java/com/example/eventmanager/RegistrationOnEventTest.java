package com.example.eventmanager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegistrationOnEventTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // Ваш базовый URL
    }

    @Test
    public void testUserCanRegisterForEvent() {
        // 1. Авторизация пользователя и получение токена
        String token = given()
                .contentType("application/json")
                .body("""
                        {
                          "login": "test",
                          "password": "test"
                        }
                      """)
                .post("/users/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("jwtToken");

        Assertions.assertNotNull(token, "Token should not be null");

        // 2. Регистрация на событие
            given()
                    .header("Authorization", "Bearer " + token)
                    .post("/events/registrations/" + 11)
                    .then()
                    .statusCode(201);

        // 3. Проверка зарегистрированных событий
        Response myRegistrations = given()
                .header("Authorization", "Bearer " + token)
                .get("/events/registrations/my")
                .then()
                .statusCode(200)
                .extract()
                .response();

        boolean isRegistered = myRegistrations.jsonPath()
                .getList("id", Long.class)
                .contains(11L);

        Assertions.assertTrue(isRegistered, "User should be registered for the event");
    }

    @Test
    public void userCanCreateEventAndCancelIt() {
        // 1. Авторизация пользователя и получение токена
        String token = given()
                .contentType("application/json")
                .body("""
                        {
                          "login": "user",
                          "password": "user"
                        }
                      """)
                .post("/users/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("jwtToken");

        Assertions.assertNotNull(token, "Token should not be null");

        // 2. Создание события
        Response newEvent = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                    {
                        "date": "2200-01-23T04:56:07.00",
                        "duration": 60,
                        "cost": 1200,
                        "maxPlaces": 10,
                        "locationId": 6,
                        "name": "Тест-Тест"
                    }
                """)
                .post("/events")
                .then()
                .statusCode(201)
                .body("name", equalTo("Тест-Тест"))
                .body("cost", equalTo(1200))
                .body("status", equalTo("WAIT_START"))
                .extract()
                .response();

        // 3. Получение мероприятия по id
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .get("/events/" + newEvent.jsonPath().getLong("id"))
                .then()
                .statusCode(200)
                .body("name", equalTo("Тест-Тест"))
                .body("cost", equalTo(1200))
                .body("status", equalTo("WAIT_START"));

        // 4. Проверка на отмену концерта
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .delete("/events/" + newEvent.jsonPath().getLong("id"))
                .then()
                .statusCode(204);
    }
}
