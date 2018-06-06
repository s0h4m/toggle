package cc.soham.toggle.objects;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("feature_metadata")
    @Expose
    public String featureMetadata;
    @SerializedName("default")
    @Expose
    public String _default;
    @SerializedName("rules")
    @Expose
    public List<Rule> rules = new ArrayList<Rule>();

    /**
     * 
     * @param rules
     *     The rules
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Feature(String name, String state, String _default, String featureMetadata, List<Rule> rules) {
        this.name = name;
        this.state = state;
        this._default = _default;
        this.featureMetadata = featureMetadata;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeatureMetadata() {
        return featureMetadata;
    }

    public void setFeatureMetadata(String featureMetadata) {
        this.featureMetadata = featureMetadata;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

    public List<Rule> getRules() {
        return rules;
    }
}
