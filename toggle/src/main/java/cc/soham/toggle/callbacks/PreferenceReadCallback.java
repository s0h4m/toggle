package cc.soham.toggle.callbacks;

import cc.soham.toggle.objects.Config;

/**
 * A callback when a preference is read
 */
public interface PreferenceReadCallback {
    void onSuccess(Config config);
    void onFailure(Exception exception);
}
