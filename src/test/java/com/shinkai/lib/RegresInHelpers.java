package com.shinkai.lib;

import io.restassured.response.Response;

import java.util.List;

public class RegresInHelpers {
    public static int getExpTotalPages(String perPage, int expTotal) {
        int perPageInt = Integer.parseInt(perPage);
        if (expTotal % perPageInt == 0) {
            return expTotal / perPageInt;
        } else {
            return (int) Math.ceil((double) expTotal / perPageInt);
        }
    }

    public static List<String> getListFromResponse(Response Response, String paramPath) {
        return Response.jsonPath().getList(paramPath);
    }

    public static int getSizeOfJsonArray(Response Response, String paramPath) {
        return Response.jsonPath().getList(paramPath).size();
    }

    public static int stringToInt(String value) {
        return Integer.parseInt(value);
    }
}
