package cc.soham.toggle.network;

import android.content.Context;

import java.io.IOException;

import cc.soham.toggle.CheckRequest;
import cc.soham.toggle.PersistUtils;
import cc.soham.toggle.callbacks.SetConfigCallback;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Manages all OkHttp related options
 * Is used only if OkHttp3 is found in the app depencies
 */
public class OkHttpUtils {
    private static OkHttpClient client;

    private static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    public static boolean isOkHttpAvailable() {
        try {
            Class cls = Class.forName("okhttp3.OkHttpClient");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A static helper method to initiate an OkHttpClient call
     * Gets the latest config and initiates a callback (optional)
     *
     * @param url
     * @param setConfigCallback
     */
    public static void startSetConfig(final Context context, final String url, final SetConfigCallback setConfigCallback) {
        if (url == null) {
            throw new IllegalStateException("Please pass a valid url");
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                SimpleConversionAndCallbackAsyncTask.handle(context, response.body().string(), setConfigCallback);
            }
        });
    }

    /**
     * A static helper method to initiate a {@link CheckLatestAsyncTask} call
     * Checks the network for the latest config and then sends the latest response to the callee
     *
     * @param checkRequest
     */
    public static void startCheck(final CheckRequest checkRequest) {
        if (checkRequest == null) {
            throw new IllegalStateException("Please pass a valid CheckRequest");
        }

        // get the url from preferences
        String url = PersistUtils.getSourceUrl(checkRequest.toggle.getContext());
        // make network request to receive response
        Request request = new Request.Builder()
                .url(url)
                .build();

        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                SimpleConversionAndCheckCallbackAsyncTask.handle(response.body().string(), checkRequest);
            }
        });
    }
}
