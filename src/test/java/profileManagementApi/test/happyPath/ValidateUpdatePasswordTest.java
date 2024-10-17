package profileManagementApi.test.happyPath;

import profileManagementApi.test.base.UpdatePasswordTest;
import annotation.DependentStep;
import environment.Environment;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import xray.Xray;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.fail;

@Test(groups = {"integration", "nonregression"})
@Xray(requirement = "IAME-14495")
public class ValidateUpdatePasswordTest extends UpdatePasswordTest {
    private static final String testId = "Validate update password";

    @Factory(dataProvider = "testData")
    public ValidateUpdatePasswordTest(Map<String, String> testData) {
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
