package utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
public class Validate {

    protected static final boolean CALL_ASSERT = true;
    private static final Object LOCK_LOGGER = new Object();

    private Validate() {

    }

    private static void logValidation(boolean isValid, String verificationContext, String message, boolean failTest) {
        synchronized (LOCK_LOGGER) {
            StringBuilder strLog = new StringBuilder();

            if (!isValid) {
                strLog.append('\n')
                        .append("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n")
                        .append("VALIDATION FAILED:\n")
                        .append("\tExpected  : ").append(verificationContext).append('\n')
                        .append("\tActual  : ").append(message).append('\n')
                        .append("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                if (log.isErrorEnabled()) {
                    log.error(strLog.toString());
                }

                if (failTest) {
                    Assert.fail(strLog.toString());
                }
            } else {
                strLog.append('\n')
                        .append("VALIDATION PASSED:\n")
                        .append("\tExpected  : ").append(verificationContext).append('\n')
                        .append("\tActual  : ").append(message);

                if (log.isInfoEnabled()) {
                    log.info(strLog.toString());
                }
            }
        }
    }

    private static boolean commonValidator(boolean isValid, String verificationContext, String message, boolean failTest) {
        logValidation(isValid, verificationContext, message, failTest);
        return isValid;
    }

    public static class Strings {

        private Strings() {
        }

        public static boolean isEmpty(String actual, String verificationContext) {
            return isEmpty(actual, verificationContext, CALL_ASSERT);
        }

        public static boolean isEmpty(String actual, String verificationContext, boolean failTest) {
            boolean isValid = actual.isEmpty();
            String message = String.format("String is empty: [%s]", actual);
            return Validate.commonValidator(isValid, verificationContext, message, failTest);
        }

        public static boolean isNotEmpty(String actual, String verificationContext) {
            return isNotEmpty(actual, verificationContext, CALL_ASSERT);
        }

        public static boolean isNotEmpty(String actual, String verificationContext, boolean failTest) {
            boolean isValid = !actual.isEmpty();
            String message = String.format("String is not empty: [%s]", actual);
            return Validate.commonValidator(isValid, verificationContext, message, failTest);
        }

        public static boolean areEqual(String expected, String actual, String verificationContext) {
            return areEqual(expected, actual, verificationContext, CALL_ASSERT);
        }

        public static boolean areEqual(String expected, String actual, String verificationContext, boolean failTest) {
            boolean isValid = expected.equals(actual);
            String message = String.format("Two strings are equal.%n\tExpected: [%s]%n\tActual  : [%s]", expected, actual);
            return Validate.commonValidator(isValid, verificationContext, message, failTest);
        }
    }

    public static class Objects {

        private Objects() {
        }

        public static boolean isNull(Object testObject, String verificationContext) {
            return isNull(testObject, verificationContext, CALL_ASSERT);
        }

        public static boolean isNull(Object testObject, String verificationContext, boolean failTest) {
            boolean isValid = testObject == null;
            String message = "Test object is null";
            return Validate.commonValidator(isValid, verificationContext, message, failTest);
        }

        public static void isNotNull(Object testObject, String verificationContext) {
            isNotNull(testObject, verificationContext, CALL_ASSERT);
        }

        public static void isNotNull(Object testObject, String verificationContext, boolean failTest) {
            boolean isValid = testObject != null;
            String message = "Test object is not null";
            Validate.commonValidator(isValid, verificationContext, message, failTest);
        }
    }
}
