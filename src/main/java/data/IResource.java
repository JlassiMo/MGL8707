package data;

import java.util.List;
import java.util.Locale;

public interface IResource {
    String get(String key);

    String get(Locale locale, String key);

    List<String> getAll();

    List<String> getAll(Locale locale);

    List<String> getSubList(String subKey);

    List<String> getSubList(Locale locale, String subKey);

    String getBaseBundleName();
}
