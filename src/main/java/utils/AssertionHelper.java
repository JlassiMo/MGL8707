package utils;

import org.assertj.core.api.Assertions;

import java.lang.reflect.InvocationTargetException;

public class AssertionHelper {

    private AssertionHelper() {
        // nothing to do
    }

    public static <T> void compareFieldByFieldRecursively(T actualResponse, T expectedResponse) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        T preparedActual = Utils.prepareForAssertion(actualResponse);
        T preparedExpected = Utils.prepareForAssertion(expectedResponse);

        Assertions.assertThat(preparedActual)
                .usingRecursiveComparison()
                .isEqualTo(preparedExpected);
    }
}
