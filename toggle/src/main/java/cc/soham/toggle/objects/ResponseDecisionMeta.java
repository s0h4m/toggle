package cc.soham.toggle.objects;

/**
 * Created by sohammondal on 21/01/16.
 */
public class ResponseDecisionMeta {
    public String state;
    public String featureMetadata;
    public String ruleMetadata;

    public ResponseDecisionMeta(Rule rule) {
        this.state = rule.state;
        this.ruleMetadata = rule.ruleMetadata;
    }

    public ResponseDecisionMeta(String state, String featureMetadata, String ruleMetadata) {
        this.state = state;
        this.featureMetadata = featureMetadata;
        this.ruleMetadata = ruleMetadata;
    }

    public ResponseDecisionMeta(String state) {
        this.state = state;
    }
}
