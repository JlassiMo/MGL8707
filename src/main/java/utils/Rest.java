package utils;

import base.AbstractDataDrivenTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class Rest extends AbstractDataDrivenTest {

    public static final String CONTEXT = "context";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_VERSION = "Accept-Version";
    public static final String CONNECTION = "Connection";
    private static final String VERSION = "1.0.0";
    private static final String KEEP_ALIVE = "keep-alive";

    public Response post(final String uriPath, String jwt, final String json, String context) {

        Response response = given().auth().oauth2(jwt).accept(ContentType.JSON)
                .contentType(ContentType.JSON).log().all().body(json).when()
                .header(CONNECTION, KEEP_ALIVE)
                .header(CONTEXT, context)
                .post(uriPath);

        response.getBody().prettyPrint();
        return response;
    }

    public Response get(final String uriPath, String jwt, String context) {
        Response response = given().auth().oauth2(jwt).accept(ContentType.JSON)
                .contentType(ContentType.JSON).log().all().when()
                .header(ACCEPT_LANGUAGE, "fr")
                .header(ACCEPT_VERSION, VERSION)
                .header(CONNECTION, KEEP_ALIVE)
                .header(CONTEXT, context)
                .get(uriPath);

        response.getBody().prettyPrint();
        return response;
    }

    public Response put(final String uriPath, final String json, final String jwt, String context) {

        Response response = given().auth().oauth2(jwt).accept(ContentType.JSON)
                .contentType(ContentType.JSON).log().all().body(json).when()
                .header(ACCEPT_LANGUAGE, "fr")
                .header(ACCEPT_VERSION, VERSION)
                .header(CONTEXT, context)
                .put(uriPath);

        response.getBody().prettyPrint();
        return response;
    }


    public String getApiUrl(String templateUrl, Map<String, String> valuesMap) {

        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(templateUrl);
    }

    public Response delete(final String uriPath, String jwt, String context) {
        Response response = given().auth().oauth2(jwt).accept(ContentType.JSON)
                .contentType(ContentType.JSON).log().all().when()
                .header(ACCEPT_LANGUAGE, "fr")
                .header(CONNECTION, KEEP_ALIVE)
                .header(CONTEXT, context)
                .delete(uriPath);

        response.getBody().prettyPrint();
        return response;
    }
}