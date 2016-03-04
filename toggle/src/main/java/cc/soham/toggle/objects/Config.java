package cc.soham.toggle.objects;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("features")
    @Expose
    public List<Feature> features = new ArrayList<Feature>();

    public Config(String name, List<Feature> features) {
        this.name = name;
        this.features = features;
    }
}
