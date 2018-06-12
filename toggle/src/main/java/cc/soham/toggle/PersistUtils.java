package cc.soham.toggle;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.net.URL;

import cc.soham.toggle.callbacks.PreferenceReadCallback;
import cc.soham.toggle.enums.SourceType;
import cc.soham.toggle.objects.Config;

/**
 * Helps persist {@link Config} on disk in some form
 */
public class PersistUtils {
    public static final String KEY_SOURCE_TYPE = "toggle_source_type";
    public static final String KEY_SOURCE_URL = "toggle_source_url";
    public static final String CONFIG_KEY = "toggle_configKey";

    /**
     * Stores the config in disk
     *
     * @param config
     */
    public static void storeConfig(final Context context, Config config) {
        String configInString = new Gson().toJson(config, Config.class);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(CONFIG_KEY, configInString).apply();
    }

    /**
     * Clears the config in disk
     */
    public static void clearConfig(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    /**
     * Retrieves the {@link Config} from disk
     * @param context
     * @param preferenceReadCallback
     */
    public static void getConfig(final Context context, final PreferenceReadCallback preferenceReadCallback) {
        PreferenceReadAsyncTask.handle(context, preferenceReadCallback);
    }

    public static Config getConfigSync(final Context context) throws Exception {
        return PreferenceReadAsyncTask.getStoredConfigStatic(context);
    }

    /**
     * Store the source type
     *
     * @param sourceType
     */
    public static void storeSourceType(final Context context, SourceType sourceType) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_SOURCE_TYPE, sourceType.name()).apply();
    }

    public static SourceType getSourceType(final Context context) {
        String source = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_SOURCE_TYPE, SourceType.NONE.name());
        return SourceType.valueOf(source);
    }

    /**
     * Store the source url
     *
     * @param url
     */
    public static void storeSourceURL(final Context context, URL url) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_SOURCE_URL, url.toExternalForm()).apply();
    }

    public static String getSourceUrl(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_SOURCE_URL, null);
    }
}
