package profileManagementApi.test.base;

import annotation.DependentStep;
import base.AbstractDataDrivenTest;
import data.TestData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpStatus;
import utils.Validate;

import java.util.Map;

public class GetProfileTest extends AbstractDataDrivenTest {

    @Test
    @DependentStep
    public void t001_Get_Student_Profile() {
        try {

            // Load server configuration
            TestData tdServerConfig = new TestData(testData).from("serverConfig.json");
            String urlTemplate = tdServerConfig.forIndex(2).getForKey("get_profile");

            // Load user data
            TestData tdUser = new TestData(testData).from("users.json");
            String studentId = tdUser.forIndex(1).getForKey("studentId");

            // Validate that the studentId and url are not null
            Validate.Objects.isNotNull(studentId, "studentId is not null");
            Validate.Objects.isNotNull(urlTemplate, "urlTemplate is not null");


            // Build the API endpoint for get profile
            String endPoint = getRestClient().getApiUrl(urlTemplate, Map.of("ProfileManagementBaseUrl", tdServerConfig.forIndex(1).getForKey("ProfileManagementBaseUrl"), "student_id", studentId));
            Validate.Objects.isNotNull(endPoint, "Endpoint is not null");

            // Perform GET request to get student profile
            Response rawResponse = getRestClient().get(endPoint);

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
