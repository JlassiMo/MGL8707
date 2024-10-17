package environment;


import base.AbstractDataDrivenTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class Environment extends AbstractDataDrivenTest {
    public static final String CONFIG_FILE_PATH = "src/test/resources/debug_config.json";
    private static String absolutePathToDataManager;

    public static Iterator<Object[]> buildTestEnvironment(String testId) {
        return buildTestData(testId);
    }

    protected static Iterator<Object[]> buildTestData(String testId) {
        List<Map<String, String>> data = buildTestDataFromJson(testId);

        return data.stream().map(map -> new Object[]{map}).iterator();
    }

    private static List<Map<String, String>> buildTestDataFromJson(String testId) {
        JSONObject dataManagerJson = getDataManagerAsJSON();

        if (dataManagerJson == null || !dataManagerJson.has(testId)) {
            log.warn("Test ID = [{}] cannot be found in Data Manager {}. Returning empty data.", testId, absolutePathToDataManager);
            return Collections.emptyList();
        }

        JSONArray dataAssignedToTest = dataManagerJson.optJSONArray(testId);
        if (dataAssignedToTest == null) {
            log.warn("Structural error: It must be an array entry like [...] for the test ID = [{}] in Data Manager {}. Data not built.", testId, absolutePathToDataManager);
            return Collections.emptyList();
        }

        List<Map<String, String>> dataListOfMaps = new ArrayList<>(dataAssignedToTest.length());
        log.info("Getting JSON test data for [{}]", testId);

        for (int i = 0; i < dataAssignedToTest.length(); ++i) {
            log.info("Data set #{}", i);
            JSONObject singleIterationData = dataAssignedToTest.optJSONObject(i);

            if (singleIterationData == null) {
                log.warn("Structural error: It must be an object entry like {...} for the iteration #{} and the test ID = [{}] in Data Manager {}. Data not built.", testId, i, absolutePathToDataManager);
                return Collections.emptyList();
            }

            Map<String, String> dataForSingleIteration = buildDataForSingleIteration(testId, i, singleIterationData);
            dataListOfMaps.add(dataForSingleIteration);
        }

        return dataListOfMaps;
    }

    private static Map<String, String> buildDataForSingleIteration(String testId, int iteration, JSONObject singleIterationData) {
        Map<String, String> retVal = new HashMap<>();

        for (String fileKey : singleIterationData.keySet()) {
            if ("descriptor".equalsIgnoreCase(fileKey) || "comment".equalsIgnoreCase(fileKey)) {
                retVal.put(fileKey, singleIterationData.getString(fileKey));
                log.info("Data {}: {}", fileKey, singleIterationData.getString(fileKey));
            } else {
                JSONArray idsJson = singleIterationData.optJSONArray(fileKey);
                if (idsJson != null) {
                    List<String> ids = idsJson.toList().stream().map(String.class::cast).toList();
                    appendDataFromFile(fileKey, ids, retVal);
                } else {
                    log.warn("Something wrong for the testId = [{}] in the data set #{} for the key {}", testId, iteration, fileKey);
                }
            }
        }

        return retVal;
    }

    private static void appendDataFromFile(String fileName, List<String> ids, Map<String, String> dataRecipient) {
        String dataFolder = new File(absolutePathToDataManager).getParent();
        if (dataFolder == null) {
            log.error("Invalid absolute path to data manager: {}", absolutePathToDataManager);
            return;
        }

        log.info("Read from data file {}", fileName);

        File dataFile = new File(dataFolder, fileName);

        try {
            // Canonicalize the paths
            String canonicalDataFolder = new File(dataFolder).getCanonicalPath();
            String canonicalDataFile = dataFile.getCanonicalPath();

            // Ensure that the file is within the expected directory
            if (!canonicalDataFile.startsWith(canonicalDataFolder)) {
                log.error("Potential path traversal attempt: {}", fileName);
                return;
            }

            if (!dataFile.exists()) {
                log.error("Data file does not exist at path: {}", dataFile.getAbsolutePath());
                return;
            }

            JSONObject dataFromFile = JSONUtils.readJSONFromFile(dataFile, StandardCharsets.UTF_8);

            for (int j = 0; j < ids.size(); ++j) {
                String id = ids.get(j);
                JSONObject dataForTest = dataFromFile.optJSONObject(id);
                if (dataForTest == null) {
                    log.warn("Data not found for ID '{}' in file '{}'", id, fileName);
                    continue;
                }

                for (String entryKey : dataForTest.keySet()) {
                    String keyToAdd = String.format("%s:%s:%d", fileName, entryKey, j + 1);
                    String valueToAdd = getValueFromJSONObject(dataForTest, entryKey);
                    dataRecipient.put(keyToAdd, valueToAdd);
                }
            }
        } catch (IOException | JSONException e) {
            log.error("Error processing data file: {} {} ", dataFile.getAbsolutePath(), e);
        }
    }

    private static String getValueFromJSONObject(JSONObject jsonObject, String key) {
        String value;
        if (!key.startsWith("as-json-")) {
            value = jsonObject.optString(key);
        } else {
            try {
                value = jsonObject.getJSONObject(key).toString();
            } catch (JSONException e) {
                value = jsonObject.getJSONArray(key).toString();
            }
        }
        return value;
    }

    private static JSONObject getDataManagerAsJSON() {
        String pathToData = validateProperty();
        if (pathToData == null) {
            return null;
        }

        return readDataManagerFromFile(pathToData);
    }

    private static JSONObject readDataManagerFromFile(String pathToData) {
        File dataManagerFile = new File(pathToData);

        try {
            // Canonicalize the path to prevent path traversal
            if (!dataManagerFile.isAbsolute()) {
                log.info("Using relative path for Data Manager file: {}", pathToData);
                String resourcePath = Objects.requireNonNull(Environment.class.getClassLoader().getResource("")).getPath();
                String fullPath = resourcePath + pathToData;

                // Safeguard against path traversal by sanitizing only the file name
                String sanitizedFileName = FilenameUtils.getName(fullPath);
                dataManagerFile = new File(FilenameUtils.getFullPathNoEndSeparator(fullPath), sanitizedFileName);
            }

            // Get canonical paths to ensure the file path is valid
            String canonicalPath = dataManagerFile.getCanonicalPath();

            // Check if the data manager file exists
            if (!dataManagerFile.exists()) {
                log.error("Data Manager file does not exist at path: {}", canonicalPath);
                return null;
            }

            // Store the absolute path of the data manager file
            absolutePathToDataManager = canonicalPath;
            log.info("Data manager file absolute path is {}", absolutePathToDataManager);

            // Read JSON from the file
            return JSONUtils.readJSONFromFile(dataManagerFile, StandardCharsets.UTF_8);

        } catch (IOException | JSONException e) {
            log.error("Error processing data manager file: {}", e.getMessage());
            return null;
        }
    }

    private static String validateProperty() {
        String pathToData = System.getProperty("bnc.data.manager");

        // Read path from config if system property is not set
        if (pathToData == null) {
            pathToData = readPathToDataFromConfig();
        }

        // Check if pathToData is still null after reading from config
        if (pathToData == null) {
            log.error("Path to data manager is null.");
            return null;
        }

        return pathToData;
    }

    private static String readPathToDataFromConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File configFile = new File(CONFIG_FILE_PATH);
            Map<String, Object> configData = objectMapper.readValue(configFile, new TypeReference<>() {
            });
            return (String) configData.get("data");
        } catch (IOException e) {
            log.error("Error reading the configuration file at {}: {}", CONFIG_FILE_PATH, e.getMessage());
            return null;
        }
    }
}