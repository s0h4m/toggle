package cc.soham.toggle.network;

import android.os.AsyncTask;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import cc.soham.toggle.CheckRequest;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.ConversionUtils;
import cc.soham.toggle.PersistUtils;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.objects.Config;

/**
 * An {@link AsyncTask} that checks the latest config for a given {@link CheckRequest}
 */
public class CheckLatestAsyncTask extends AsyncTask<Void, Void, CheckResponse> {
    final CheckRequest checkRequest;

    public CheckLatestAsyncTask(CheckRequest checkRequest) {
        this.checkRequest = checkRequest;
    }

    /**
     * Attempt to download the config file and process the feature (wrt the config) here
     * Also return cached responses wherever the network is not available
     *
     * @param params
     * @return
     */
    @Override
    protected CheckResponse doInBackground(Void... params) {
        return getCheckResponse(checkRequest);
    }

    /**
     * Initiating the {@link cc.soham.toggle.callbacks.Callback} is on the UI thread, when applicable
     *
     * @param checkResponse the {@link CheckResponse} after downloading and processing
     */
    @Override
    protected void onPostExecute(final CheckResponse checkResponse) {
        NetworkUtils.initiateCallbackAfterCheck(checkResponse, checkRequest);
    }

    /**
     * Generates a {@link CheckResponse} for a given {@link CheckRequest}
     *
     * @param checkRequest the given {@link CheckRequest} which needs to be downloaded and processed
     * @return the resultant {@link CheckResponse} after downloading and processing (or cached in case of errors)
     */
    private static CheckResponse getCheckResponse(CheckRequest checkRequest) {
        try {
            // get the url from preferences
            String url = PersistUtils.getSourceUrl(checkRequest.toggle.getContext());
            // make network request to receive response
            String response = NetworkUtils.downloadUrl(url);
            // convert string to config
            Config config = ConversionUtils.convertStringToConfig(response);
            // store config
            Toggle.storeConfigInMem(config);
            PersistUtils.storeConfig(checkRequest.toggle.getContext(), config);
            // process the resultant config
            CheckResponse result = checkRequest.toggle.processConfig(config, checkRequest);
            // disable the cache flag since this is a live request
            result.cached = false;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkRequest.toggle.getAndProcessCachedConfigSync(checkRequest);
    }

    /**
     * A static helper method to initiate a {@link CheckLatestAsyncTask} call
     * Checks the network for the latest config and then sends the latest response to the callee
     *
     * @param checkRequest
     */
    public static void start(final CheckRequest checkRequest) {
        if (checkRequest == null) {
            throw new IllegalStateException("Please pass a valid CheckRequest");
        }
        new CheckLatestAsyncTask(checkRequest).execute();
    }
}
