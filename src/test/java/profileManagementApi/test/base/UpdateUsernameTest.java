package profileManagementApi.test.base;

import profileManagementApi.pretest.ProfileManagementBaseTest;
import annotation.DependentStep;
import environment.Environment;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import xray.Xray;

import java.util.Iterator;

@Xray(requirement = "IAME-14496")
@Test(groups = {"integration", "nonregression"})
public class UpdateUsernameTest extends ProfileManagementBaseTest {

    @Test
    @DependentStep
    public void t002_Update_Username() {

        try {

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
