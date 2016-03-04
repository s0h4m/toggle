package cc.soham.toggle;

/**
 * Represents a response to a feature check request
 */
public class CheckResponse {
    public String featureName;
    public String state;
    public String featureMetadata;
    public String ruleMetadata;
    public boolean cached = true;

    public CheckResponse(String featureName, String state, String featureMetadata, String ruleMetadata) {
        this.featureName = featureName;
        this.state = state;
        this.featureMetadata = featureMetadata;
        this.ruleMetadata = ruleMetadata;
    }

    public CheckResponse(String featureName, String state, String featureMetadata, String ruleMetadata, boolean cached) {
        this.featureName = featureName;
        this.state = state;
        this.featureMetadata = featureMetadata;
        this.ruleMetadata = ruleMetadata;
        this.cached = cached;
    }
}
