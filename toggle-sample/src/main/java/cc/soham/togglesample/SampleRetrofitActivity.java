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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.soham.toggle.Toggle;
import cc.soham.toggle.CheckResponse;
import cc.soham.toggle.objects.Config;
import cc.soham.togglesample.network.MyApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Sample Retrofit Activity, shows how to
 * - Use {@link retrofit2.Retrofit} to make a network call and manually retrieve a {@link Config} configuration
 * - Once the config is retrieved, use {@link Toggle#setConfig(Config)} to store the config in {@link Toggle}
 * - To check for the feature, use {@link Toggle#check(String)} to check for the status of the feature
 *
 * Note: {@link Toggle#check(String)} can be called before/after {@link Toggle#setConfig(Config)} is called
 * Here {@link Toggle#check(String)} will check for cache and the default value in the Request to check for the feature
 */
public class SampleRetrofitActivity extends AppCompatActivity implements ProgressBarInterface {
    @Bind(R.id.activity_sample_feature)
    Button featureButton;
    @Bind(R.id.activity_sample_feature_rule_metadata)
    TextView ruleMetadataTextView;
    @Bind(R.id.activity_sample_feature_feature_metadata)
    TextView featureMetadataTextView;
    @Bind(R.id.activity_sample_feature_cached)
    TextView cachedTextView;
    @Bind(R.id.activity_sample_feature_progress)
    ProgressBar progressBar;

    private String featureToBeChecked = "mixpanel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_base);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Shows how to manually create a {@link retrofit2.Retrofit} network call to retrieve a {@link Config} config
     * Once the {@link Config} config is received, we manually store it in Toggle using {@link Toggle#setConfig(Config)}
     */
    @OnClick(R.id.activity_sample_set_config)
    public void setConfigButton_onClick() {
        showMessage("Making a retrofit call to get the config");
        progressBar.setVisibility(View.VISIBLE);
        Call<Config> configCall = MyApi.getApi().getConfig();
        configCall.enqueue(new Callback<Config>() {
            @Override
            public void onResponse(final Response<Config> response) {
                Toast.makeText(SampleRetrofitActivity.this, "Retrofit response received, storing in toggle", Toast.LENGTH_SHORT).show();
                Config config = response.body();
                Toggle.with(SampleRetrofitActivity.this).setConfig(config);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                showMessage( "Error: " + t != null ? t.getMessage() : "Unknown");
            }
        });
    }

    /**
     * Shows how to check for a particular feature, here we check for the 'mixpanel' feature in the config
     * We can pass in additional flags in the {@link Toggle#check(String)} call like default value etc.
     */
    @OnClick(R.id.activity_sample_check)
    public void checkButton_onClick() {
        showMessage("Checking for the feature "+ featureToBeChecked);
        progressBar.setVisibility(View.VISIBLE);
        Toggle.with(SampleRetrofitActivity.this).check(featureToBeChecked).defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                showMessage("Feature checked");
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetadata, checkResponse.ruleMetadata, checkResponse.cached);
                progressBar.setVisibility(View.GONE);
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
        featureButton.setText(state == Toggle.ENABLED ? Toggle.ENABLED : Toggle.DISABLED);
        featureButton.setEnabled(state == Toggle.ENABLED);
        featureMetadataTextView.setText("Feature Metadata: " + ruleMetadata);
        ruleMetadataTextView.setText("Rule Metadata: " + ruleMetadata);
        cachedTextView.setText("Cached: " + cached);
    }

    /**
     * Simple helper method to show Toasts
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(SampleRetrofitActivity.this, message, Toast.LENGTH_SHORT).show();
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
