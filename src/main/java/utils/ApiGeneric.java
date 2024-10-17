package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiGeneric {
    private ApiGeneric() {
    }

    public static <T> T objectMapper(JSONObject json, Class<T> valueType) throws IOException {
        return new ObjectMapper().readValue(json.toString(), valueType);
    }

    public static JsonPath rawResponseToJsonPath(Response response) {
        String responseAsString = response.asString();
        return new JsonPath(responseAsString);
    }

    public static <T> List<T> jsonArrayToArrayList(JSONArray json, Class<T> valueType) throws IOException {
        ArrayList<T> bankAccountList = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            T bankAccount = objectMapper(json.getJSONObject(i), valueType);
            bankAccountList.add(bankAccount);
        }
        return bankAccountList;
    }
}