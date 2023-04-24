package com.shinkai.lib;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, Response.statusCode(), "Status code is not as expected");
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }


    public static void assertJsonByNameContains(Response Response, String name, String substring) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertThat(value, containsString(substring));
    }

    public static void assertJsonSchema(Response Response, String jsonSchema) {
        Response.then().assertThat().body(matchesJsonSchemaInClasspath("schemes/" + jsonSchema));
    }

    public static void assertListValuesEndWith(List<String> elements, String ending) {
        assertTrue((elements.stream().allMatch(el -> el.endsWith(ending))));
    }

    public static void assertListValuesStartWith(List<String> elements, String start) {
        assertTrue((elements.stream().allMatch(el -> el.startsWith(start))));
    }

    public static void assertIntNotNullAndLessThanOrEqualTo(int checkNum, int numEqualOrMore) {
        assertThat(checkNum, allOf(greaterThan(0), lessThanOrEqualTo(numEqualOrMore)));
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(expectedAnswer, Response.asString(), "Response is not as expected");
    }
}
