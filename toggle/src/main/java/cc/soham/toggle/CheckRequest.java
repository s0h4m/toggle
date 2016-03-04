package cc.soham.toggle;

import android.support.annotation.VisibleForTesting;

import cc.soham.toggle.callbacks.Callback;
import cc.soham.toggle.callbacks.ErrorCallback;

/**
 * Represents a feature check request
 * Called as a challenge to the config for the given feature name
 */
public class CheckRequest {
    // Required parameters
    public final Toggle toggle;
    public final String featureName;
    public final Callback callback;

    // Optional parameters
    public final String defaultState;
    public final boolean getLatest;
    public final ErrorCallback errorCallback;

    public static class Builder {
        // Required parameters
        private final Toggle toggle;
        private final String featureName;
        private Callback callback;

        // Optional parameters
        String defaultState = Toggle.DEFAULT_STATE;
        boolean getLatest;
        ErrorCallback errorCallback;

        public Builder(Toggle toggle, String featureName) {
            this.toggle = toggle;
            this.featureName = featureName;
        }

        /**
         * Sets the default state of this request
         * @param defaultState
         * @return
         */
        public Builder defaultState(String defaultState) {
            this.defaultState = defaultState;
            return this;
        }

        /**
         * Should we check for the latest config before delivering the result
         * NOTE: only applicable for {@link cc.soham.toggle.enums.SourceType#URL}
         * @return
         */
        public Builder getLatest() {
            this.getLatest = true;
            return this;
        }

        /**
         * An optional error parameter to provide a callback in case an error occurs
         *
         * @param errorCallback
         * @return
         */
        public Builder onError(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;
            return this;
        }

        /**
         * Use this method to get the status of an individual feature
         *
         * @param callback
         * @return
         */
        public CheckRequest start(Callback callback) {
            if (featureName == null) {
                throw new IllegalStateException("You need to provide a feature name to provide a callback when that feature is checked");
            }
            this.callback = callback;
            return new CheckRequest(this);
        }
    }

    private CheckRequest(Builder builder) {
        this.toggle = builder.toggle;
        this.featureName = builder.featureName;
        this.callback = builder.callback;
        this.defaultState = builder.defaultState;
        this.getLatest = builder.getLatest;
        this.errorCallback = builder.errorCallback;
        toggle.handleFeatureCheckRequest(this);
    }

    /**
     * This constructor is to be used purely for testing
     *
     * @param toggle
     * @param featureName
     * @param callback
     * @param defaultState
     * @param getLatest
     * @param errorCallback
     */
    @VisibleForTesting
    CheckRequest(Toggle toggle, String featureName, Callback callback, String defaultState, boolean getLatest, ErrorCallback errorCallback) {
        this.toggle = toggle;
        this.featureName = featureName;
        this.callback = callback;
        this.defaultState = defaultState;
        this.getLatest = getLatest;
        this.errorCallback = errorCallback;
    }
}
