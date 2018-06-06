package cc.soham.toggle.network;

import android.os.AsyncTask;

import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.ConversionUtils;
import cc.soham.toggle.CheckRequest;
import cc.soham.toggle.PersistUtils;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.objects.Config;

/**
 * A simple AsyncTask to
 * - convert the string response of the {@link Config} to a Config
 * - store it in memory and disk
 * - initiate a callback on the UI thread
 */
public class SimpleConversionAndCheckCallbackAsyncTask extends AsyncTask<Void, Void, CheckResponse> {
    final String configInString;
    final CheckRequest checkRequest;

    public SimpleConversionAndCheckCallbackAsyncTask(String configInString, CheckRequest checkRequest) {
        this.configInString = configInString;
        this.checkRequest = checkRequest;
    }

    @Override
    protected CheckResponse doInBackground(Void... params) {
        // convert string to config
        Config config = ConversionUtils.convertStringToConfig(configInString);
        // generate the map
        config.generateFeatureMap();
        // store config
        Toggle.storeConfigInMem(config);
        PersistUtils.storeConfig(checkRequest.toggle.getContext(), config);
        // process the resultant config
        CheckResponse result = checkRequest.toggle.processConfig(config, checkRequest);
        // disable the cache flag since this is a live request
        result.cached = false;
        return result;
    }

    @Override
    protected void onPostExecute(CheckResponse checkResponse) {
        // initiate a callback on the UI thread
        NetworkUtils.initiateCallbackAfterCheck(checkResponse, checkRequest);
    }

    /**
     * Converts, stores and initates a callback for a given OkHttp3 response
     * @param configInString
     * @param checkRequest
     */
    public static void handle(final String configInString, final CheckRequest checkRequest) {
        new SimpleConversionAndCheckCallbackAsyncTask(configInString, checkRequest).execute();
    }
}
