package cc.soham.toggle;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import cc.soham.toggle.callbacks.PreferenceReadCallback;
import cc.soham.toggle.objects.Config;

/**
 * An {@link AsyncTask} to read a preference
 */
public class PreferenceReadAsyncTask extends AsyncTask<Void, Void, Config> {
    private final Context context;
    private final PreferenceReadCallback preferenceReadCallback;
    private Exception exception;

    public PreferenceReadAsyncTask(Context context, PreferenceReadCallback preferenceReadCallback) {
        if (context == null || preferenceReadCallback == null)
            throw new IllegalStateException("The context and the exception cannot be null");
        this.context = context;
        this.preferenceReadCallback = preferenceReadCallback;
    }

    @Override
    protected Config doInBackground(Void... params) {
        try {
            Config config = getStoredConfigStatic(context);
            return config;
        } catch (Exception exception) {
            this.exception = exception;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Config config) {
        if (config != null) {
            preferenceReadCallback.onSuccess(config);
        } else {
            if (exception == null) {
                exception = new IllegalStateException("Config could not be retrieved");
            }
            preferenceReadCallback.onFailure(exception);
        }
    }

    @Nullable
    public static Config getStoredConfigStatic(Context context) {
        Config config = null;

        String configInString = PreferenceManager.getDefaultSharedPreferences(context).getString(PersistUtils.CONFIG_KEY, null);
        if (configInString != null) {
            config = ConversionUtils.convertStringToConfig(configInString);
        }

        return config;
    }

    public static void handle(final Context context, PreferenceReadCallback preferenceReadCallback) {
        new PreferenceReadAsyncTask(context, preferenceReadCallback).execute();
    }
}
