package cc.soham.toggle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.JsonElement;

import java.net.URL;

import cc.soham.toggle.callbacks.PreferenceReadCallback;
import cc.soham.toggle.callbacks.SetConfigCallback;
import cc.soham.toggle.enums.SourceType;
import cc.soham.toggle.network.CheckLatestAsyncTask;
import cc.soham.toggle.network.OkHttpUtils;
import cc.soham.toggle.network.SetConfigAsyncTask;
import cc.soham.toggle.objects.Feature;
import cc.soham.toggle.objects.Config;
import cc.soham.toggle.objects.ResponseDecisionMeta;
import cc.soham.toggle.objects.Rule;

/**
 * The core class that handles all Toggle functionality
 */
public class Toggle {
    // TODO: integrate leak cananary in the sample?
    // TODO: unit tests for the new memcache (store config in memcache for all conditions)
    // TODO: unit tests for file and rule metadata
    // TODO: unit test okhttp implementation
    // TODO: instrument test okhttp implementation
    // TODO: improve documentation
    // TODO: create wiki

    public static final String DEFAULT_STATE = "default";
    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";

    @VisibleForTesting
    static volatile Toggle singleton;

    public static Toggle with(final Context context) {
        if (singleton == null) {
            synchronized (Toggle.class) {
                singleton = new Toggle(context);
            }
        }
        return singleton;
    }

    private Context context;
    private SourceType sourceType;
    private Config config;

    public static void storeConfigInMem(Config config) {
        if (singleton != null) {
            singleton.config = config;
        }
    }

    public static Config getConfig() {
        if(singleton!=null) {
            return singleton.config;
        }
        return null;
    }

    public Toggle(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        this.context = context.getApplicationContext();
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public void setConfig(String configInString) {
        setSourceType(SourceType.STRING);
        // store source
        PersistUtils.storeSourceType(getContext(), SourceType.STRING);
        // convert from string to config
        Config config = ConversionUtils.convertStringToConfig(configInString);
        // store config
        storeConfigInMem(config);
        PersistUtils.storeConfig(getContext(), config);
    }

    public void setConfig(JsonElement configInJson) {
        setSourceType(SourceType.JSONOBJECT);
        // store source
        PersistUtils.storeSourceType(getContext(), SourceType.JSONOBJECT);
        // convert from json to config
        Config config = ConversionUtils.convertJSONObjectToConfig(configInJson);
        // store config
        storeConfigInMem(config);
        PersistUtils.storeConfig(getContext(), config);
    }

    public void setConfig(Config config) {
        setSourceType(SourceType.CONFIG);
        // store source
        PersistUtils.storeSourceType(getContext(), SourceType.CONFIG);
        // store config
        storeConfigInMem(config);
        PersistUtils.storeConfig(getContext(), config);
    }

    public void setConfig(URL configUrl) {
        setConfig(configUrl, null);
    }

    public void setConfig(URL configUrl, SetConfigCallback setConfigCallback) {
        setSourceType(SourceType.URL);
        // store source
        PersistUtils.storeSourceType(getContext(), SourceType.URL);
        PersistUtils.storeSourceURL(getContext(), configUrl);
        // make the network request and store the results
        if (OkHttpUtils.isOkHttpAvailable()) {
            OkHttpUtils.startSetConfig(getContext(), configUrl.toExternalForm(), setConfigCallback);
        } else {
            SetConfigAsyncTask.start(getContext(), configUrl.toExternalForm(), setConfigCallback);
        }
    }

    public CheckRequest.Builder check(String featureName) {
        return new CheckRequest.Builder(singleton, featureName);
    }

    // core methods

    /**
     * Handles a checkRequest
     * Called once a check is initiated for a given feature (name)
     *
     * @param checkRequest
     */
    public void handleFeatureCheckRequest(final CheckRequest checkRequest) {
        if (sourceType == null) {
            sourceType = PersistUtils.getSourceType(getContext());
        }
        if (!sourceType.equals(SourceType.URL) || !checkRequest.getLatest) {
            getAndProcessCachedConfig(checkRequest);
        } else {
            // make network checkRequest
            makeNetworkFeatureCheckRequest(checkRequest);
        }
    }

    // TODO: need to unit test this again in light of mem cache
    public void getAndProcessCachedConfig(final CheckRequest checkRequest) {
        if (config == null) {
            // get cached or get default
            PersistUtils.getConfig(getContext(), new PreferenceReadCallback() {
                @Override
                public void onSuccess(Config config) {
                    // store in mem
                    Toggle.storeConfigInMem(config);
                    // call the success method
                    configRetrievedSuccess(config, checkRequest);
                }

                @Override
                public void onFailure(Exception exception) {
                    // couldnt retrieve a stored config, send back the default response
                    exception.printStackTrace();
                    configRetrievedFailure(checkRequest);
                }
            });
        } else {
            // call the success method
            configRetrievedSuccess(config, checkRequest);
        }
    }

    private void configRetrievedSuccess(Config config, CheckRequest checkRequest) {
        CheckResponse checkResponse = processConfig(config, checkRequest);
        // make the callback
        makeFeatureCheckCallback(checkRequest, checkResponse);
    }

    private void configRetrievedFailure(CheckRequest checkRequest) {
        // send back a default response
        String state = DEFAULT_STATE;
        if (checkRequest.defaultState != null) {
            state = checkRequest.defaultState;
        }
        checkRequest.callback.onStatusChecked(new CheckResponse(checkRequest.featureName, state, null, null, true));
    }

    public CheckResponse getAndProcessCachedConfigSync(final CheckRequest checkRequest) {
        Config config = null;
        try {
            config = PersistUtils.getConfigSync(getContext());
        } catch (Exception exception) {
            exception.printStackTrace();
            return getExceptionCheckResponse(checkRequest);
        }
        // check for null
        if (config == null) {
            if (checkRequest.defaultState == null) {
                throw new IllegalStateException("No configuration found (Config) and no default state configured in the state check");
            }
            return new CheckResponse(checkRequest.featureName, checkRequest.defaultState, null, null, true);
        }
        // process the config
        CheckResponse checkResponse = processConfig(config, checkRequest);
        return checkResponse;

    }

    @NonNull
    @VisibleForTesting
        // TODO: unit test getExceptionCheckResponse
    CheckResponse getExceptionCheckResponse(CheckRequest checkRequest) {
        return new CheckResponse(checkRequest.featureName, DEFAULT_STATE, null, null, true);
    }

    @VisibleForTesting
        // TODO: unit test makeFeatureCheckCallback
    void makeFeatureCheckCallback(CheckRequest checkRequest, CheckResponse checkResponse) {
        checkRequest.callback.onStatusChecked(checkResponse);
    }

    /**
     * Code for enabling/disabling a feature based on a request ({@link CheckRequest}) and a config ({@link Config})
     *
     * @param config
     * @param checkRequest
     * @return
     */
    public CheckResponse processConfig(Config config, CheckRequest checkRequest) {
        for (Feature feature : config.features) {
            // find the given feature in the received Config
            if (feature.name.equals(checkRequest.featureName)) {
                ResponseDecisionMeta responseDecisionMeta = handleFeature(feature, checkRequest);
                // if there is a decisive state (either enabled or disabled) initiate the callback and break
                return new CheckResponse(checkRequest.featureName, responseDecisionMeta.state, responseDecisionMeta.featureMetadata, responseDecisionMeta.ruleMetadata);
            }
        }
        // a) feature not found or b) no state could be made based on the config
        // check if there was no default state in the request, send enabled (default toggle)
        String state;
        if (checkRequest.defaultState == null) {
            state = DEFAULT_STATE;
        } else {
            state = checkRequest.defaultState;
        }
        return new CheckResponse(checkRequest.featureName, state, null, null, false);
    }

    /**
     * makes a network request for the given params
     */
    public void makeNetworkFeatureCheckRequest(final CheckRequest checkRequest) {
        if (OkHttpUtils.isOkHttpAvailable()) {
            OkHttpUtils.startCheck(checkRequest);
        } else {
            CheckLatestAsyncTask.start(checkRequest);
        }
    }

    /**
     * Get the result of handling a given feature
     * (when a match between a requested feature and the store feature is found)
     *
     * @param feature
     * @param checkRequest
     * @return
     */
    @VisibleForTesting
    ResponseDecisionMeta handleFeature(final Feature feature, final CheckRequest checkRequest) {
        ResponseDecisionMeta responseDecisionMeta;
        if (feature.state == null) {
            if (feature.rules == null) {
                throw new IllegalStateException("You must have rules in case the feature does not have a base state");
            }
            // state is null, so we can check the rules
            for (Rule rule : feature.rules) {
                if (RuleMatcher.matchRule(rule)) {
                    // if a rule is matched
                    // return an enum which contains
                    // a) whether we should enable/disable the feature
                    // b) the ruleMetadata
                    responseDecisionMeta = getRuleMatchedResponseDecision(rule);
                    responseDecisionMeta.featureMetadata = feature.featureMetadata;
                    return responseDecisionMeta;
                }
            }
            // no rule match, return the default state of the feature
            responseDecisionMeta = getDefaultResponseDecision(feature, checkRequest);
            responseDecisionMeta.featureMetadata = feature.featureMetadata;
            return responseDecisionMeta;
        } else {
            // if state is not null, that means we need to just set this feature to 'that' state and ignore all rules
            responseDecisionMeta = getStatePoweredResponseDecision(feature);
            responseDecisionMeta.featureMetadata = feature.featureMetadata;
            return responseDecisionMeta;
        }
    }

    @NonNull
    @VisibleForTesting
    ResponseDecisionMeta getRuleMatchedResponseDecision(Rule rule) {
        // an enabled/disabled state is mandatory for a rule
        // (else we wouldn't know what to do once a rule is matched)
        if (rule.state == null)
            throw new IllegalStateException("The state in a Rule cannot be empty");
        return new ResponseDecisionMeta(rule);
    }

    @NonNull
    @VisibleForTesting
    ResponseDecisionMeta getStatePoweredResponseDecision(Feature feature) {
        // if state is null, we can't take state on this
        if (feature.state == null)
            return new ResponseDecisionMeta(DEFAULT_STATE);
        // if state is not null, use the overriding feature
        return new ResponseDecisionMeta(feature.state);
    }

    @NonNull
    @VisibleForTesting
    ResponseDecisionMeta getDefaultResponseDecision(final Feature feature, final CheckRequest checkRequest) {
        // first preference in defaults is given to the local variable (provided while the Toggle.with().check().setDefault().handle() call)
        if (checkRequest.defaultState != null) {
            return new ResponseDecisionMeta(checkRequest.defaultState);
        }
        // in case no Default is provided in the feature (in the config), return enabled
        if (feature._default == null) {
            return new ResponseDecisionMeta(DEFAULT_STATE);
        }
        // so no local defaults but some default is provided in the config
        return new ResponseDecisionMeta(feature._default);
    }

    public Context getContext() {
        return context;
    }
}