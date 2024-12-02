package profileManagementApi.test.base;

import base.AbstractDataDrivenTest;
import annotation.DependentStep;
import data.TestData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpStatus;
import utils.Validate;

import java.util.Map;

public class UpdatePasswordTest extends AbstractDataDrivenTest {

    @Test
    @DependentStep
    public void t001_Update_Password() {

        try {

        } catch (Exception ex) {
            logger.catching(ex);
            Assert.fail(ex.getMessage());
        }
    }
}
