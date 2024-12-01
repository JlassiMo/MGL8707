package profileManagementApi.test.base;

import annotation.DependentStep;
import org.testng.Assert;
import org.testng.annotations.Test;
import profileManagementApi.pretest.ProfileManagementBaseTest;

public class GetProfileTest extends ProfileManagementBaseTest {

    @Test
    @DependentStep
    public void t002_Get_Profile() {
        try {

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
