package profileManagementApi.test.happyPath;

import annotation.DependentStep;
import data.TestData;
import environment.Environment;
import io.restassured.response.Response;
import models.UsernameUpdateResponse;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import profileManagementApi.test.base.UpdateUsernameTest;
import utils.ApiGeneric;
import utils.AssertionHelper;
import utils.Validate;
import xray.Xray;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.fail;

@Xray(requirement = "UQAM-14496")
@Test(groups = {"integration", "nonregression"})
public class ValidateUpdateUsernameTest extends UpdateUsernameTest {

    private static final String testId = "Validate update username";

    @Factory(dataProvider = "testData")
    public ValidateUpdateUsernameTest(Map<String, String> testData) {
        this.testData = testData;
    }

    @DataProvider(name = "testData")
    public static Iterator<Object[]> getTestData() {
        return Environment.buildTestEnvironment(testId);
    }
    @Test
    @DependentStep
    public void t003_Validate_Response_Json_Body_Structure() {
        try {
            // Extract the raw response from the world context
            Response rawResponse = pullFromTheWorld(WorldKey.RAW_RESPONSE, Response.class);
            Validate.Objects.isNotNull(rawResponse, "Response is not null");

            // Convert the raw response to a UsernameUpdateResponse object using its body
            UsernameUpdateResponse actualResponse = rawResponse.getBody().as(UsernameUpdateResponse.class);

            // Validate that the actual response is not null
            Validate.Objects.isNotNull(actualResponse, "The actual response is not null");

            // Store the actual response in the world context
            pushToTheWorld(WorldKey.ACTUAL_RESPONSE, actualResponse);

        } catch (Exception ex) {
            logger.catching(ex);
            fail(ex.getMessage());
        }
    }
    @Test
    @DependentStep
    public void t004_Validate_Response_Body_Student_Profile_Fields() {

        try {
            // Load profile data from the test data for a specific index
            TestData tdUpdateProfile = new TestData(testData).from("updateProfile.json").forIndex(1);

            // Extract the actual response from the world context
            UsernameUpdateResponse actualResponse = pullFromTheWorld(WorldKey.ACTUAL_RESPONSE, UsernameUpdateResponse.class);

            // Validate that the actual response is not null
            Validate.Objects.isNotNull(actualResponse, "The actual response is not null");

            // Get the expected response as JSON from test data
            JSONObject expectedResponseAsJson = tdUpdateProfile.getFormattedForKey("as-json-update-username-response-body", JSONObject.class);
            Validate.Objects.isNotNull(expectedResponseAsJson, "Expected JSON response is not null");

            // Convert the expected JSON response to a UsernameUpdateResponse object
            UsernameUpdateResponse expectedResponse = ApiGeneric.objectMapper(expectedResponseAsJson, UsernameUpdateResponse.class);

            // Compare actual and expected responses field by field recursively
            AssertionHelper.compareFieldByFieldRecursively(actualResponse, expectedResponse);
        } catch (Exception ex) {
            logger.catching(ex);
            fail(ex.getMessage());
        }
    }
}
