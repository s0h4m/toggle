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
}
