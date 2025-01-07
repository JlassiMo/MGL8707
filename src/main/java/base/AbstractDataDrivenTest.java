package base;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Listeners;
import utils.RestClient;

@Listeners(listener.TestngListener.class)
public abstract class AbstractDataDrivenTest {
    protected final Logger logger = LogManager.getLogger(this.getClass().getName());
    protected Map<String, String> testData = new HashMap<>();
    private static RestClient restClient;
    private static ThreadLocal<Map<String, Object>> worldLocal = new ThreadLocal<>();

    public static <E extends Enum<E>> void pushToTheWorld(E key, Object value) {
        if (worldLocal.get() == null) {
            worldLocal.set(new HashMap<>());
        }
        worldLocal.get().put(key.name(), value);
    }

    public static <T, E extends Enum<E>> T pullFromTheWorld(E key, Class<T> valueClass) {
        Map<String, Object> world = worldLocal.get();
        if (world != null && world.containsKey(key.name())) {
            return valueClass.cast(world.get(key.name()));
        } else {
            Logger logger = LogManager.getLogger(AbstractDataDrivenTest.class);
            if (logger.isErrorEnabled()) {
                logger.error("The key [{}] is not defined in the world. Returning null.", key.name());
            }
            return null;
        }
    }

    public static <E extends Enum<E>> boolean isDefinedInTheWorld(E key) {
        Map<String, Object> world = worldLocal.get();
        return world != null && world.containsKey(key.name());
    }

    public static void clearWorldLocal() {
        worldLocal.remove();
    }

    public enum WorldKey {
        RAW_RESPONSE, ACTUAL_RESPONSE, EXPECTED_RESPONSE, ROBOT_TOKEN, TEST_DATA, CLIENT_ID, CONTEXT
    }
    // Protected getter to allow subclasses to access the RestClient
    public static synchronized RestClient getRestClient() {
        if (restClient == null)
            restClient = new RestClient();
        return restClient;
    }
}
