package data;

import exception.TestDataException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import transformer.DataTransformer;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TestData {
    private final Map<String, String> internalData;
    private String source = null;
    private String key = null;
    private int index = -1;
    private IResource resource = null;

    public TestData(Map<String, String> initialTestData) {
        this.internalData = initialTestData != null ? Collections.unmodifiableMap(initialTestData) : Collections.emptyMap();
    }

    public TestData from(String source) {
        this.source = source;
        return this;
    }

    public TestData forKey(String key) {
        this.key = key;
        return this;
    }

    public TestData forIndex(int index) {
        this.index = index;
        return this;
    }

    public TestData withResource(IResource resource) {
        this.resource = resource;
        return this;
    }

    private void reset(boolean doReset) {
        if (doReset) {
            this.source = null;
            this.key = null;
            this.index = -1;
            this.resource = null;
        }
    }

    public <T> T getFormattedForKey(String key, Class<T> valueClass) {
        String oldValue = this.key;
        this.key = key;
        T retVal = this.getFormattedAndReset(false, valueClass);
        this.key = oldValue;
        return retVal;
    }

    private String getAndReset(boolean doReset) {
        try {
            String retVal;
            if (this.resource != null) {
                retVal = this.tdGetDataByKey(this.source, this.key, this.index, this.resource);
                if (retVal == null) {
                    log.warn("No test data found for the source = {}, key = {}, index = {}, resource = {}. Returning null.", this.source, this.key, this.index, this.resourceFile());
                }
            } else {
                retVal = this.tdGetDataByKey(this.source, this.key, this.index);
                if (retVal == null) {
                    log.warn("No test data found for the source = {}, key = {}, index = {}. Returning null.", this.source, this.key, this.index);
                }
            }
            return retVal;
        } catch (Exception e) {
            log.warn("Error while getting test data for the source = {}, key = {}, index = {}", this.source, this.key, this.index);
            return null;
        } finally {
            this.reset(doReset);
        }
    }

    public String getForKey(String key) {
        String oldValue = this.key;
        this.key = key;
        String retVal = this.getAndReset(false);
        this.key = oldValue;
        return retVal;
    }

    private <T> T getFormattedAndReset(boolean doReset, Class<T> valueClass) {
        String keySetting = this.key;
        String value = this.getAndReset(doReset);

        try {
            if (keySetting.startsWith("as-json-")) {
                return parseJsonValue(valueClass, value);
            }
            return castValue(valueClass, value);
        } catch (ClassCastException e) {
            log.warn("Error while casting test data to {} for the source = {}, key = {}, index = {}", valueClass.getName(), this.source, this.key, this.index);
        } catch (Exception e) {
            log.warn("Error while getting formatted test data for the source = {}, key = {}, index = {}", this.source, this.key, this.index);
        }
        return null;
    }

    private <T> T parseJsonValue(Class<T> valueClass, String value) {
        if (valueClass.isAssignableFrom(JSONObject.class)) {
            return valueClass.cast(new JSONObject(value));
        } else if (valueClass.isAssignableFrom(JSONArray.class)) {
            return valueClass.cast(new JSONArray(value));
        } else {
            throw new ClassCastException("Unsupported JSON value class: " + valueClass.getName());
        }
    }

    private <T> T castValue(Class<T> valueClass, String value) {
        if (valueClass.isAssignableFrom(String.class)) {
            return valueClass.cast(value);
        } else if (valueClass.isAssignableFrom(Boolean.class) || valueClass == Boolean.TYPE) {
            return valueClass.cast(Boolean.valueOf(value));
        } else if (valueClass.isAssignableFrom(Integer.class) || valueClass == Integer.TYPE) {
            return valueClass.cast(Integer.valueOf(value));
        } else if (valueClass.isAssignableFrom(Long.class) || valueClass == Long.TYPE) {
            return valueClass.cast(Long.valueOf(value));
        } else if (valueClass.isAssignableFrom(Double.class) || valueClass == Double.TYPE) {
            return valueClass.cast(Double.valueOf(value));
        } else {
            throw new ClassCastException("Unsupported value class: " + valueClass.getName());
        }
    }


    public String asString() {
        return this.internalData.entrySet().stream().map(entry -> entry.getKey() + " = " + entry.getValue()).collect(Collectors.joining("\n", "{", "}"));
    }

    private String tdGetDataByKey(String dataFile, String key, int dataOrder) {
        String fullKey = String.format("%s:%s:%d", dataFile, key, dataOrder);
        String retVal = internalData.get(fullKey);
        if (retVal == null) {
            throw new TestDataException("Cannot get test data by key " + fullKey + ",\ntest data:\n" + new JSONObject(internalData).toString(2));
        } else {
            try {
                return DataTransformer.transform(retVal);
            } catch (Exception e) {
                return retVal;
            }
        }
    }

    private String tdGetDataByKey(String dataFile, String key, int dataOrder, IResource resource) {
        return resource.get(this.tdGetDataByKey(dataFile, key, dataOrder));
    }

    private String resourceFile() {
        if (this.resource == null) {
            return null;
        } else {
            try {
                return this.resource.getBaseBundleName();
            } catch (Exception e) {
                return "unknown";
            }
        }
    }

    public String toString() {
        return String.format("Test Data:%nSource = %s%nKey = %s%nIndex = %d%nResource = %s%nData = %s", this.source, this.key, this.index, this.resourceFile(), this.asString());
    }
}
