package profileManagementApi.test.happyPath;

import profileManagementApi.test.base.GetProfileTest;
import annotation.DependentStep;
import environment.Environment;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import xray.Xray;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.fail;

@Xray(requirement = "IAME-14497")
@Test(groups = {"integration", "nonregression"})
public class ValidateGetProfileTest extends GetProfileTest {

    private static final String testId = "Validate get profile";

    @Factory(dataProvider = "testData")
    public ValidateGetProfileTest(Map<String, String> testData) {
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


        } catch (Exception ex) {
            logger.catching(ex);
            fail(ex.getMessage());
        }
    }

    @Test
    @DependentStep
    public void t004_Validate_Response_Body_Student_Profile_Fields() {

        try {


        } catch (Exception ex) {
            logger.catching(ex);
            fail(ex.getMessage());
        }
    }
}
