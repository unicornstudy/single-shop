package com.unicornstudy.singleshop.exception.util;

import java.util.HashMap;
import java.util.Map;

public class BodyCreator {

    public static Map<String, String> createErrorBody(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);

        return response;
    }
}
