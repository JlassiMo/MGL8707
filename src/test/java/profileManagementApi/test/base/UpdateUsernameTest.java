package profileManagementApi.test.base;

import annotation.DependentStep;
import base.AbstractDataDrivenTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import xray.Xray;

@Xray(requirement = "IAME-14496")
@Test(groups = {"integration", "nonregression"})
public class UpdateUsernameTest extends AbstractDataDrivenTest {

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
