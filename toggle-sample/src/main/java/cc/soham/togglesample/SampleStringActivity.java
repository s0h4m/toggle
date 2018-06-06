package cc.soham.togglesample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.objects.Config;

/**
 * Sample String Activity, shows how to
 * - Input configuration using a {@link String} resource (This should conform to the standard {@link Config}
 * json schema
 * - To check for the feature, use {@link Toggle#check(String)} to check for the status of the feature
 */
public class SampleStringActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_base);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Shows how to use {@link Toggle#setConfig(String)} to get the config from a {@link String} resource
     * The string input must follow the standard {@link Config} json structure
     */
    @OnClick(R.id.activity_sample_set_config)
    public void setConfigButton_onClick() {
        Toast.makeText(SampleStringActivity.this, "Importing config from a local String object", Toast.LENGTH_SHORT).show();
        try {
            Toggle.with(this).setConfig("{\"name\": \"jsappbasics\", \"features\":[{\"name\":\"video\", \"state\":\"disabled\", \"default\": \"enabled\", \"rules\":[{\"state\": \"disabled\", \"value\": {\"apilevel_min\": 21, \"apilevel_max\": 23, \"appversion_min\": 11, \"appversion_max\": 13, \"date_min\": 1452766668000, \"date_max\": 1455566668000, \"buildtype\":\"debug\", \"device\":[{\"manufacturer\":\"xiaomi\",\"model\":\"mi3\"}, {\"manufacturer\":\"samsung\", \"model\":\"s4\"}]}}, {\"state\": \"disabled\", \"value\": {\"appversion_max\": 13}}]},{\"name\":\"crash_reporting\", \"rules\":[{\"state\": \"disabled\", \"value\": {\"appversion\": 11, \"buildtype\": \"debug\"}}]},{\"name\":\"mixpanel\",\"state\": \"enabled\"}]}");
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
        showMessage("Checking for the feature");
        Toggle.with(SampleStringActivity.this).check("mixpanel").defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                showMessage("Feature checked");
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetadata, checkResponse.ruleMetadata, checkResponse.cached);
            }
        });
    }

    /**
     * Update the UI as per the feature state
     *
     * @param feature  Name of the feature
     * @param state    The feature-toggle state of the feature: state/disabled
     * @param featureMetadata Feature Metadata attached to the feature
     * @param ruleMetadata Rule Metadata attached to the feature
     * @param cached   Shows whether this is a cached response or not
     */
    private void updateUiAfterResponse(String feature, String state, String featureMetadata, String ruleMetadata, boolean cached) {
        featureButton.setText(feature + " is " + (state == Toggle.ENABLED ? "enabled" : "disabled"));
        featureButton.setEnabled(state == Toggle.ENABLED);
        featureMetadataTextView.setText("Feature Metadata: " + ruleMetadata);
        ruleMetadataTextView.setText("Rule Metadata: " + ruleMetadata);
        cachedTextView.setText("Cached: " + cached);
    }


    /**
     * Simple helper method to show Toasts
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(SampleStringActivity.this, message, Toast.LENGTH_SHORT).show();
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
}
