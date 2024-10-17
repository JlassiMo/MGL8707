package profileManagementApi.test.base;

import profileManagementApi.pretest.ProfileManagementBaseTest;
import annotation.DependentStep;
import data.TestData;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpStatusCodes;
import utils.Scope;
import utils.Validate;

import java.util.Map;

import static utils.Jwt.getOktaTokenProfileManagementWithPrivateKey;

public class UpdatePasswordTest extends ProfileManagementBaseTest {

    @Test
    @DependentStep
    public void t002_Update_Password() {

        try {


        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
