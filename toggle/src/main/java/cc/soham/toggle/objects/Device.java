package cc.soham.toggle.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("manufacturer")
    @Expose
    public String manufacturer;
    @SerializedName("model")
    @Expose
    public String model;

    public Device(String manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }
}
