package profileManagementApi.test.happyPath;

import environment.Environment;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import profileManagementApi.test.base.UpdateUsernameTest;
import xray.Xray;

import java.util.Iterator;
import java.util.Map;
@Xray(requirement = "IAME-14496")
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
}
