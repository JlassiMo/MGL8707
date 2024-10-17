package profileManagementApi.pretest;

import annotation.DependentStep;
import base.AbstractDataDrivenTest;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Rest;
import utils.Scope;
import utils.Validate;

import static utils.Jwt.getOktaTokenProfileManagementWithPrivateKey;

public class ProfileManagementBaseTest extends AbstractDataDrivenTest {
    protected Rest rest = new Rest();

    @Test
    public void t000_Setup_Token() {

        try {
            RestAssured.useRelaxedHTTPSValidation();

            String robotToken = getOktaTokenProfileManagementWithPrivateKey(testData, Scope.CIAM_TPPS_WRITE);

            Validate.Objects.isNotNull(robotToken, "robotToken is not null");
            pushToTheWorld(WorldKey.ROBOT_TOKEN, robotToken);

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    @DependentStep
    public void t001_Create_Profile() {

        try {

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void t999_Delete_Tpps() {
        try {

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
