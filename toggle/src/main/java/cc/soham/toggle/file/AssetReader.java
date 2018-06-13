package cc.soham.toggle.file;

import android.content.Context;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Responsible for reading from an asset file
 * TODO: test this
 */
public class AssetReader {
    public interface OnAssetReadListener {
        void onAssetRead(@NonNull String asset);
    }

    /**
     * Read from the asset file into a String on a worker thread
     * @param context
     * @param fileName
     * @return
     */
    @NonNull
    @WorkerThread
    static String readFromAssetFile(@NonNull Context context, @NonNull String fileName) {
        StringBuilder value = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                value.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return value.toString();
    }

    /**
     * Reads a String from an asset file on any thread
     * @param context
     * @param fileName
     * @param onAssetReadListener
     */
    @AnyThread
    public static void readFromAssetFile(@NonNull final Context context, @NonNull final String fileName, @NonNull final OnAssetReadListener onAssetReadListener) {
        new Thread() {
            @Override
            public void run() {
                String asset = readFromAssetFile(context, fileName);
                onAssetReadListener.onAssetRead(asset);
            }
        }.start();
    }
}
