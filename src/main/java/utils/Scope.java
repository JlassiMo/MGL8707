package utils;

import java.util.HashMap;
import java.util.Map;

public enum Scope {

    CIAM_TPPS_WRITE("uqam:tpps:write"),
    CIAM_TPPS_READ("uqam:tpps:read"),
    CIAM_TPPS_DELETE("uqam:tpps:delete");

    private final String value;
    private static final Map<String, Scope> lookup = new HashMap<>();

    private Scope(String scope) {
        this.value = scope;
    }

    static {
        for (Scope scope : Scope.values()) {
            lookup.put(scope.getValue(), scope);
        }
    }

    public String getValue() {
        return value;
    }

    public static Scope fromString(String stringValue) {
        return lookup.get(stringValue);
    }
}