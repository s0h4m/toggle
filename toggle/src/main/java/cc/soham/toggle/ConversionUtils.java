package cc.soham.toggle;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import cc.soham.toggle.objects.Config;

/**
 * Various methods assisting conversion from String/JsonElement to {@link Config}
 */
public class ConversionUtils {
    /**
     * Converts String to Config
     *
     * @param configInString
     * @return
     */
    public static Config convertStringToConfig(String configInString) throws JsonSyntaxException {
        return new Gson().fromJson(configInString, Config.class);
    }

    /**
     * Converts JSON to Config
     *
     * @param configInJson
     * @return
     */
    public static Config convertJSONObjectToConfig(JsonElement configInJson) {
        return new Gson().fromJson(configInJson, Config.class);
    }
}
