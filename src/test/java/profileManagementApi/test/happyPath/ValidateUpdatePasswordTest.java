package profileManagementApi.test.happyPath;

import annotation.DependentStep;
import data.TestData;
import environment.Environment;
import io.restassured.response.Response;
import models.PasswordUpdateResponse;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import profileManagementApi.test.base.UpdatePasswordTest;
import utils.ApiGeneric;
import utils.AssertionHelper;
import utils.Validate;
import xray.Xray;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.fail;

@Test(groups = {"integration", "regression"})
@Xray(requirement = "UQAM-14494")
public class ValidateUpdatePasswordTest extends UpdatePasswordTest {
    private static final String TEST_ID = "Validate update password";

    @Factory(dataProvider = "testData")
    public ValidateUpdatePasswordTest(Map<String, String> testData) {
        this.testData = testData;
    }

    @DataProvider(name = "testData")
    public static Iterator<Object[]> getTestData() {
        return Environment.buildTestEnvironment(TEST_ID);
    }

    @Test
    @DependentStep
    public void t003_Validate_Response_Json_Body_Structure() {
        try {
            // Extract the raw response from the world context
            Response rawResponse = pullFromTheWorld(WorldKey.RAW_RESPONSE, Response.class);
            Validate.Objects.isNotNull(rawResponse, "Response is not null");

            // Convert the raw response to a PasswordUpdateResponse object using its body
            PasswordUpdateResponse actualResponse = rawResponse.getBody().as(PasswordUpdateResponse.class);

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
    public void t004_Validate_Response_Body_Update_Password_Fields() {

        try {
            // Load profile data from the test data for a specific index
            TestData tdUpdateProfile = new TestData(testData).from("updateProfile.json").forIndex(1);

            // Extract the actual response from the world context
            PasswordUpdateResponse actualResponse = pullFromTheWorld(WorldKey.ACTUAL_RESPONSE, PasswordUpdateResponse.class);

            // Validate that the actual response is not null
            Validate.Objects.isNotNull(actualResponse, "The actual response is not null");

            // Get the expected response as JSON from test data
            JSONObject expectedResponseAsJson = tdUpdateProfile.getFormattedForKey("as-json-update-password-response-body", JSONObject.class);
            Validate.Objects.isNotNull(expectedResponseAsJson, "Expected JSON response is not null");

            // Convert the expected JSON response to a PasswordUpdateResponse object
            PasswordUpdateResponse expectedResponse = ApiGeneric.objectMapper(expectedResponseAsJson, PasswordUpdateResponse.class);

            // Compare actual and expected responses field by field recursively
            AssertionHelper.compareFieldByFieldRecursively(actualResponse, expectedResponse);
        } catch (Exception ex) {
            logger.catching(ex);
            fail(ex.getMessage());
        }
    }
}
