package cc.soham.toggle.callbacks;

import cc.soham.toggle.CheckResponse;

/**
 * A callback triggered when a check is performed
 */
public interface Callback {
    void onStatusChecked(CheckResponse checkResponse);
}
