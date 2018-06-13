package cc.soham.toggle.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cc.soham.toggle.CheckRequest;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.callbacks.SetConfigCallback;

/**
 * Performs various Network operations
 */
public class NetworkUtils {
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public static String downloadUrl(@NonNull String myurl) throws IOException {
        InputStream is = null;
        String contentAsString;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            contentAsString = readIt(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return contentAsString;
    }

    // Reads an InputStream and converts it to a String.
    private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initiate a {@link SetConfigCallback} if needed
     *
     * @param setConfigResponse
     * @param setConfigCallback
     */
    public static void initiateCallback(@Nullable SetConfigResponse setConfigResponse, @Nullable SetConfigCallback setConfigCallback) {
        // make the callback if configured
        if (setConfigCallback != null && setConfigResponse != null) {
            setConfigCallback.onConfigReceived(setConfigResponse.config, setConfigResponse.cached);
        }
    }

    /**
     * Initate a {@link cc.soham.toggle.callbacks.Callback} if needed
     *
     * @param checkResponse
     * @param checkRequest
     */
    public static void initiateCallbackAfterCheck(@Nullable CheckResponse checkResponse, @Nullable CheckRequest checkRequest) {
        // make the callback if configured
        if (checkRequest != null && checkRequest.callback != null) {
            if (checkResponse != null) {
                checkRequest.callback.onStatusChecked(checkResponse);
            } else {
                checkRequest.callback.onStatusChecked(new CheckResponse(checkRequest.featureName, checkRequest.defaultState, null, null, true));
            }
        }
    }
}
