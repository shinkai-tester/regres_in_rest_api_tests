package com.shinkai.tests;

import com.shinkai.generators.UserDataGenerator;
import com.shinkai.lib.ApiRequests;
import com.shinkai.lib.Assertions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shinkai.lib.RegresInHelpers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RegresInTests {

    private final ApiRequests apiRequests = new ApiRequests();
    private final UserDataGenerator generator = new UserDataGenerator();


    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Check that it is possible to create user with all fields: name, username, email, job and avatar")
    @DisplayName("Successful user creation with all data")
    public void testRegisterNewUser() {

        String name = generator.getFullName();
        String job = generator.getJob();
        String email = generator.getEmail();
        String avatar = generator.getAvatarLink();
        String username = generator.getUsername();
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("username", username);
        body.put("job", job);
        body.put("email", email);
        body.put("avatar", avatar);

        Response responseCreateUser = apiRequests.makePostRequest("/api/users", body);

        Assertions.assertJsonSchema(responseCreateUser, "createUserScheme.json");
        Assertions.assertResponseCodeEquals(responseCreateUser, 201);
        Assertions.assertJsonHasField(responseCreateUser, "id");
        Assertions.assertJsonHasField(responseCreateUser, "createdAt");
        assertAll("Checking actual and expected username, firstName, lastName and email",
                () -> Assertions.assertJsonByName(responseCreateUser, "name", name),
                () -> Assertions.assertJsonByName(responseCreateUser, "username", username),
                () -> Assertions.assertJsonByName(responseCreateUser, "job", job),
                () -> Assertions.assertJsonByName(responseCreateUser, "email", email),
                () -> Assertions.assertJsonByName(responseCreateUser, "avatar", avatar)
        );
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Check that it is possible to update user's job")
    @DisplayName("Successful update of user's job")
    public void testUpdateJob() {
        String userId = "3";
        String job = generator.getJob();
        Map<String, String> body = new HashMap<>();
        body.put("job", job);

        Response updateJobResponse = apiRequests.makePutRequest("/api/users/", userId, body);

        Assertions.assertJsonSchema(updateJobResponse, "updateUserScheme.json");
        Assertions.assertResponseCodeEquals(updateJobResponse, 200);
        Assertions.assertJsonByName(updateJobResponse, "job", job);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Check that it is possible to delete a user")
    @DisplayName("Successful user deletion")
    public void testDeleteUser() {
        String userId = "5";

        Response deleteUserResponse = apiRequests.makeDeleteRequest("/api/users/", userId);

        Assertions.assertResponseCodeEquals(deleteUserResponse, 204);
    }


    @ValueSource(strings = {"email", "password"})
    @ParameterizedTest(name = "Unsuccessful user registration: missing parameter {0}")
    @Severity(SeverityLevel.NORMAL)
    @Description("Check that it is not possible to register a user if one of the parameters is missing")
    public void testRegisterWithoutOneParam(String parameter) {
        String email = "lindsay.ferguson@reqres.in";
        String password = generator.getPassword();
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        body.remove(parameter);

        Response registerUser = apiRequests.makePostRequest("/api/register", body);

        Assertions.assertResponseCodeEquals(registerUser, 400);
        Assertions.assertJsonByNameContains(registerUser, "error", "Missing " + parameter);
    }

    @ParameterizedTest(name = "Get list of users with per_page={0}")
    @ValueSource(strings = {"6", "5", "12", "1"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Check that it is possible to get users' list with their data")
    public void testGetUsersList(String perPage) {
        int expTotal = 12;
        int expTotalPages = getExpTotalPages(perPage, expTotal);

        Response getUsersList = apiRequests.makeGetRequestWithPerPage("/api/users/", perPage);

        Assertions.assertJsonSchema(getUsersList, "listOfUsersScheme.json");
        Assertions.assertResponseCodeEquals(getUsersList, 200);
        Assertions.assertJsonByName(getUsersList, "page", 1);
        Assertions.assertJsonByName(getUsersList, "total", expTotal);
        Assertions.assertJsonByName(getUsersList, "per_page", stringToInt(perPage));
        Assertions.assertJsonByName(getUsersList, "total_pages", expTotalPages);

        List<String> emailsFromResp = getListFromResponse(getUsersList, "data.email");
        List<String> avatarsFromResp = getListFromResponse(getUsersList, "data.avatar");
        int actualNumOfUsers = getSizeOfJsonArray(getUsersList, "data");
        Assertions.assertListValuesEndWith(emailsFromResp, "@reqres.in");
        Assertions.assertListValuesStartWith(avatarsFromResp, "https://reqres.in/img/faces/");
        Assertions.assertIntNotNullAndLessThanOrEqualTo(stringToInt(perPage), actualNumOfUsers);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check data of user with id which doesn't exist")
    @DisplayName("Get data of unknown user")
    public void testGetUnknownUser() {
        String userId = "100";

        Response getUnknownUser = apiRequests.makeGetRequestWithId("/api/users/", userId);

        Assertions.assertResponseCodeEquals(getUnknownUser, 404);
        Assertions.assertResponseTextEquals(getUnknownUser, "{}");
    }
}
