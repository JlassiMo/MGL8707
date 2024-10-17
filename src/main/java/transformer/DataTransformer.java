package transformer;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Slf4j
public class DataTransformer {
    private static final String SENSITIVE_MARKER = "$en$itive";

    private DataTransformer() {
        throw new UnsupportedOperationException("DataTransformer is a utility class and cannot be instantiated.");
    }
    public static String transform(String value) {
        try {
            // Do not transform value if there is no sensitive marker
            if (!value.startsWith(SENSITIVE_MARKER)) {
                return value;
            }

            final String key = value.substring(value.lastIndexOf(':') + 1);

            String envVariableValue = System.getenv(key);
            if (envVariableValue != null) {
                return envVariableValue;
            }
            log.error("Environment variable '{}' not found.", key);

        } catch (Exception ex) {
            log.error("Error transforming value.", ex);
        }
        return value;
    }
}
