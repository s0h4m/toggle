package cc.soham.toggle.network;

import android.content.Context;
import android.os.AsyncTask;

import cc.soham.toggle.ConversionUtils;
import cc.soham.toggle.PersistUtils;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.callbacks.SetConfigCallback;
import cc.soham.toggle.objects.Config;

/**
 * An {@link AsyncTask} that helps convert a string to config and then store it in mem/disk
 */
public class SimpleConversionAndCallbackAsyncTask extends AsyncTask<Void, Void, SetConfigResponse> {
    final String configInString;
    final SetConfigCallback setConfigCallback;
    final Context context;

    public SimpleConversionAndCallbackAsyncTask(final Context context, String configInString, SetConfigCallback setConfigCallback) {
        this.context = context;
        this.configInString = configInString;
        this.setConfigCallback = setConfigCallback;
    }

    @Override
    protected SetConfigResponse doInBackground(Void... params) {
        Config config = ConversionUtils.convertStringToConfig(configInString);
        Toggle.storeConfigInMem(config);
        PersistUtils.storeConfig(context, config);
        return new SetConfigResponse(config);
    }

    @Override
    protected void onPostExecute(SetConfigResponse setConfigResponse) {
        NetworkUtils.initiateCallback(setConfigResponse, setConfigCallback);
    }

    public static void handle(final Context context, final String configInString, final SetConfigCallback setConfigCallback) {
        new SimpleConversionAndCallbackAsyncTask(context, configInString, setConfigCallback).execute();
    }
}
