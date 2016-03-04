package cc.soham.toggle.callbacks;

import java.net.URL;

import cc.soham.toggle.objects.Config;

/**
 * Callback triggered after a network setConfig call via {@link cc.soham.toggle.Toggle#setConfig(URL, SetConfigCallback)}
 * is made
 */
public interface SetConfigCallback {
    void onConfigReceived(Config config, boolean cached);
}
