package cc.soham.toggle.network;

import cc.soham.toggle.objects.Config;

/**
 * Response of {@link SetConfigAsyncTask}
 */
public class SetConfigResponse {
    Config config;
    boolean cached = false;

    public SetConfigResponse(Config config) {
        this.config = config;
    }

    public SetConfigResponse(Config config, boolean cached) {
        this.config = config;
        this.cached = cached;
    }
}
