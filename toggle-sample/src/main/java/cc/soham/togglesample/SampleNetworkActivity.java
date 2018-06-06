package cc.soham.togglesample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.soham.toggle.CheckRequest;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.callbacks.SetConfigCallback;
import cc.soham.toggle.objects.Config;

/**
 * Sample Network Activity, shows how to
 * - Use {@link Toggle#setConfig(Config)} to make a network call and manually retrieve and  astore {@link Config} configuration using {@link }
 * - To check for the feature, use {@link Toggle#check(String)} to check for the status of the feature
 * - To check for the feature with the latest config, use {@link Toggle#check(String)} with the {@link CheckRequest.Builder#getLatest()} flag to check for the
 * latest status of the feature
 * <p/>
 * We implement {@link ProgressBarInterface} to enable easy Espresso integration tests via
 * {@link ProgressBarInterface}
 */
public class SampleNetworkActivity extends AppCompatActivity implements ProgressBarInterface {
    @BindView(R.id.activity_sample_feature)
    Button featureButton;
    @BindView(R.id.activity_sample_feature_rule_metadata)
    TextView ruleMetadataTextView;
    @BindView(R.id.activity_sample_feature_feature_metadata)
    TextView featureMetadataTextView;
    @BindView(R.id.activity_sample_feature_cached)
    TextView cachedTextView;
    @BindView(R.id.activity_sample_feature_progress)
    ProgressBar progressBar;

    private String featureToBeChecked = "mixpanel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_network);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Shows how to use {@link Toggle#setConfig(Config)} to get the config from
     * Once the {@link Config} config is received, we manually store it in Toggle using {@link Toggle#setConfig(Config)}
     */
    @OnClick(R.id.activity_sample_set_config)
    public void setConfigButton_onClick() {
        Toast.makeText(SampleNetworkActivity.this, "Making a network call to get the config", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        try {
            Toggle.with(this).setConfig(new URL(Constants.URL_CONFIG), new SetConfigCallback() {
                @Override
                public void onConfigReceived(Config config, boolean cached) {
                    Toast.makeText(SampleNetworkActivity.this, "Response received, storing in toggle", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows how to check for a particular feature, here we check for the 'mixpanel' feature in the config
     * We can pass in additional flags in the {@link Toggle#check(String)} call like default value etc.
     */
    @OnClick(R.id.activity_sample_check)
    public void checkButton_onClick() {
        showMessage("Checking for the feature " + featureToBeChecked);
        progressBar.setVisibility(View.VISIBLE);
        Toggle.with(SampleNetworkActivity.this).check(featureToBeChecked).defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                showMessage("Feature checked");
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetadata, checkResponse.ruleMetadata, checkResponse.cached);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /*
     * Shows how to check for the latest status of a  particular feature, here we check for the 'mixpanel' feature in the config
     * We can pass in additional flags in the {@link Toggle#check(String)} call like default value etc.
     */
    @OnClick(R.id.activity_sample_check_latest)
    public void checkLatestButton_onClick() {
        showMessage("Checking for the feature " + featureToBeChecked);
        progressBar.setVisibility(View.VISIBLE);
        Toggle.with(SampleNetworkActivity.this).check(featureToBeChecked).getLatest().defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                showMessage("Latest feature checked");
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetadata, checkResponse.ruleMetadata, checkResponse.cached);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Update the UI as per the feature state
     *
     * @param feature         Name of the feature
     * @param state           The feature-toggle state of the feature: state/disabled
     * @param featureMetadata Feature Metadata attached to the feature
     * @param ruleMetadata    Rule Metadata attached to the feature
     * @param cached          Shows whether this is a cached response or not
     */
    private void updateUiAfterResponse(String feature, String state, String featureMetadata, String ruleMetadata, boolean cached) {
        featureButton.setText(state == Toggle.ENABLED ? Toggle.ENABLED : Toggle.DISABLED);
        featureButton.setEnabled(state == Toggle.ENABLED);
        featureMetadataTextView.setText("Feature Metadata: " + featureMetadata);
        ruleMetadataTextView.setText("Rule Metadata: " + ruleMetadata);
        cachedTextView.setText("Cached: " + cached);
    }

    /**
     * Simple helper method to show Toasts
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(SampleNetworkActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
