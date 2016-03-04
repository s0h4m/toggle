package cc.soham.toggle.callbacks;

/**
 * A callback called in case of error
 */
public interface ErrorCallback {
    void onError(Exception exception, String feature, boolean enabled, String metadata);
}
