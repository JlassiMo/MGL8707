package profileManagementApi.test.base;

import annotation.DependentStep;
import base.AbstractDataDrivenTest;
import data.TestData;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpStatus;
import utils.Validate;
import xray.Xray;

import java.util.Map;

@Xray(requirement = "IAME-14496")
@Test(groups = {"integration", "nonregression"})
public class UpdateUsernameTest extends AbstractDataDrivenTest {

    @Test
    @DependentStep
    public void t002_Update_Username() {

        try {
            // Load server configuration
            TestData tdServerConfig = new TestData(testData).from("serverConfig.json");
            String urlTemplate = tdServerConfig.forIndex(2).getForKey("put_username");


            // Validate that url is not null
            Validate.Objects.isNotNull(urlTemplate, "urlTemplate is not null");


            // Build the API endpoint for get profile
            String endPoint = getRestClient().getApiUrl(urlTemplate, Map.of("ProfileManagementBaseUrl", tdServerConfig.forIndex(1).getForKey("ProfileManagementBaseUrl")));
            Validate.Objects.isNotNull(endPoint, "Endpoint is not null");

            // Load payload from json file
            TestData tdUpdateProfile = new TestData(testData).from("updateProfile.json");
            // Prepare payload for update password
            JSONObject requestAsJsonUpdateUsername = tdUpdateProfile.forIndex(1).getFormattedForKey("as-json-update-username-request-payload", JSONObject.class);
            Validate.Objects.isNotNull(requestAsJsonUpdateUsername, "Request body is not null");

            // Perform PUT request to update username
            Response rawResponse = getRestClient().put(endPoint, requestAsJsonUpdateUsername.toString());

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
