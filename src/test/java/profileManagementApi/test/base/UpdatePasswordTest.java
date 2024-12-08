package profileManagementApi.test.base;

import base.AbstractDataDrivenTest;
import annotation.DependentStep;
import data.TestData;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpStatus;
import utils.RestClient;
import utils.Validate;

import java.util.Map;

public class UpdatePasswordTest extends AbstractDataDrivenTest {

    @Test
    @DependentStep
    public void t001_Update_Password() {

        try {

            // Load server configuration
            TestData tdServerConfig = new TestData(testData).from("serverConfig.json");
            String urlTemplate = tdServerConfig.forIndex(2).getForKey("put_password");


            // Validate that url is not null
            Validate.Objects.isNotNull(urlTemplate, "urlTemplate is not null");


            // Build the API endpoint for get profile
            String endPoint = getRestClient().getApiUrl(urlTemplate, Map.of("ProfileManagementBaseUrl", tdServerConfig.forIndex(1).getForKey("ProfileManagementBaseUrl")));
            Validate.Objects.isNotNull(endPoint, "Endpoint is not null");

            // Load payload from json file
            TestData tdScope = new TestData(testData).from("updateProfile.json");
            // Prepare payload for update password
            JSONObject requestAsJsonUpdatePassword = tdScope.forIndex(1).getFormattedForKey("as-json-update-password-request-payload", JSONObject.class);
            Validate.Objects.isNotNull(requestAsJsonUpdatePassword, "Request body is not null");


            // Perform PUT request to update password
            Response rawResponse = getRestClient().put(endPoint, requestAsJsonUpdatePassword.toString());

            // Validate that the response is not null
            Validate.Objects.isNotNull(rawResponse, "The response is not null");

            // Log the status code of the response
            logger.info("Status Code is: [{}] ", rawResponse.statusCode());

            // Assert that the status code of the response is as expected
            rawResponse.then().assertThat().statusCode(HttpStatus.OK.getCode());

            // Store the raw response in the world context
            pushToTheWorld(WorldKey.RAW_RESPONSE, rawResponse);
        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
