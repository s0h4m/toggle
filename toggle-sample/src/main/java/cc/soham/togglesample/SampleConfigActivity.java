package cc.soham.togglesample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.objects.Config;
import cc.soham.toggle.objects.Feature;
import cc.soham.toggle.objects.Rule;
import cc.soham.toggle.objects.Value;

/**
 * Sample Config Activity, shows how to
 * - Use {@link Toggle#setConfig(Config)} to configure Toggle
 * - To check for the feature, use {@link Toggle#check(String)} to check for the status of the feature
 */
public class SampleConfigActivity extends AppCompatActivity {
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
     * Shows how to configure Toggle by using a {@link Config} object
     */
    @OnClick(R.id.activity_sample_set_config)
    public void setConfigButton_onClick() {
        showMessage("Importing configuration via a Config object");
        Config config = createSampleConfig();
        Toggle.with(SampleConfigActivity.this).setConfig(config);
        showMessage("Importing configuration via a Config object");
    }

    /**
     * Shows how to check for a particular feature, here we check for the 'mixpanel' feature in the config
     * We can pass in additional flags in the {@link Toggle#check(String)} call like default value etc.
     */
    @OnClick(R.id.activity_sample_check)
    public void checkButton_onClick() {
        showMessage("Checking for the feature");
        Toggle.with(SampleConfigActivity.this).check("mixpanel").defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
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
        Toast.makeText(SampleConfigActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates a Sample Config object with two sample rules
     *
     * @return
     */
    @NonNull
    private static Config createSampleConfig() {
        List<Rule> rules = new ArrayList<>();
        Value value1 = new Value(14, 23, null, null, null, null, null, null);
        Value value2 = new Value(null, null, null, null, 1453196880000L, null, null, null);

        String metadata = "sample ruleMetadata";
        rules.add(new Rule(Toggle.DISABLED, metadata, value1));
        rules.add(new Rule(Toggle.DISABLED, metadata, value2));

        Feature featureVideo = new Feature("video", null, Toggle.ENABLED, null, rules);
        Feature featureAudio = new Feature("mixpanel", null, Toggle.ENABLED, null, rules);
        Feature featureSpeech = new Feature("speech", null, Toggle.DISABLED, null, rules);

        List<Feature> features = new ArrayList<>();
        features.add(featureVideo);
        features.add(featureAudio);
        features.add(featureSpeech);

        return new Config("myapp", features);
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