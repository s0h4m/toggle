package cc.soham.togglesample;

import android.support.test.espresso.IdlingResource;
import android.view.View;

/**
 * A simple {@link IdlingResource} that can be used across the toggle samples
 */
public class ProgressBarIdlingResource implements IdlingResource {
    ResourceCallback resourceCallback;
    ProgressBarInterface progressBarInterface;

    public ProgressBarIdlingResource(ProgressBarInterface progressBarInterface) {
        this.progressBarInterface = progressBarInterface;
    }

    @Override
    public String getName() {
        return "setConfig";
    }

    /**
     * Return idle when the {@link android.widget.ProgressBar} is not visible anymore
     */
    @Override
    public boolean isIdleNow() {
        if(progressBarInterface.getProgressBar().getVisibility() != View.VISIBLE) {
            resourceCallback.onTransitionToIdle();
            return true;
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
