package com.shinkai.lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class ApiRequests {

    final String BASE_URI = "https://reqres.in";

    @Step("Make a POST-request")
    public Response makePostRequest(String path, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .contentType(JSON)
                .body(data)
                .post(path)
                .andReturn();
    }

    @Step("Make a PUT-request to update user's data")
    public Response makePutRequest(String path, String id, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .contentType(JSON)
                .body(data)
                .put(path + id)
                .andReturn();
    }

    @Step("Make a DELETE user request")
    public Response makeDeleteRequest(String path, String id) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .delete(path + id)
                .andReturn();
    }

    @Step("Make a GET-request with per_page")
    public Response makeGetRequestWithPerPage(String path, String per_page) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .get(path + "?per_page=" + per_page)
                .andReturn();
    }

    @Step("Make a GET-request with id")
    public Response makeGetRequestWithId(String path, String id) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .get(path + id)
                .andReturn();
    }
}
