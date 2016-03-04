package cc.soham.toggle;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by sohammondal on 19/01/16.
 */
public class DeviceModelUtils {
    /**
     * Returns the consumer friendly device name
     * TODO: unit test getModel
     */
    public static String getModel(String manufacturer, String model) {
        if (manufacturer == null)
            return model;
        if (model.startsWith(manufacturer)) {
            return model.substring(manufacturer.length());
        }
        return model;
    }
}
