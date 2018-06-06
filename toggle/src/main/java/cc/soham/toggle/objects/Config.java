package cc.soham.toggle.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("features")
    @Expose
    public List<Feature> features = new ArrayList<Feature>();
    // a map representing the list of features, supports lookup in constant time O(1)
    public transient Map<String, Feature> featureMap = new HashMap<>();

    public Config(String name, List<Feature> features) {
        this.name = name;
        this.features = features;
        generateFeatureMap();
    }

    /**
     * Generates a {@link Map} out of the features {@link List}
     */
    public void generateFeatureMap() {
        featureMap = new HashMap<>();
        if (features != null) {
            for (Feature feature : features) {
                featureMap.put(feature.getName(), feature);
            }
        }
    }

    /**
     * Gets the {@link Map} of features for lookup use
     * @return
     */
    public Map<String, Feature> getFeatureMap() {
        return featureMap;
    }
}
