package utils;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class JSONUtils {

    private JSONUtils() {
    }

    public static JSONObject readJSONFromFile(File fileToRead, Charset charset) throws IOException, JSONException {
        String s = FileUtils.readFileToString(fileToRead, charset);
        return new JSONObject(s);
    }
}
