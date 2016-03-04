package cc.soham.toggle.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rule {

    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("ruleMetadata")
    @Expose
    public String ruleMetadata;
    @SerializedName("value")
    @Expose
    public Value value;

    public Rule(String state, String ruleMetadata, Value value) {
        this.state = state;
        this.ruleMetadata = ruleMetadata;
        this.value = value;
    }
}
