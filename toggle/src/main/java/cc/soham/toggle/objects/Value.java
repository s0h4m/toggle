package cc.soham.toggle.objects;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("apilevel_min")
    @Expose
    public Integer apilevelMin;
    @SerializedName("apilevel_max")
    @Expose
    public Integer apilevelMax;
    @SerializedName("appversion_min")
    @Expose
    public Integer appversionMin;
    @SerializedName("appversion_max")
    @Expose
    public Integer appversionMax;
    @SerializedName("date_min")
    @Expose
    public Long dateMin;
    @SerializedName("date_max")
    @Expose
    public Long dateMax;
    @SerializedName("buildtype")
    @Expose
    public String buildtype;
    @SerializedName("device")
    @Expose
    public List<Device> device = new ArrayList<>();

    public Value(Integer apilevelMin, Integer apilevelMax, Integer appversionMin, Integer appversionMax, Long dateMin, Long dateMax, String buildtype, List<Device> device) {
        this.apilevelMin = apilevelMin;
        this.apilevelMax = apilevelMax;
        this.appversionMin = appversionMin;
        this.appversionMax = appversionMax;
        this.dateMin = dateMin;
        this.dateMax = dateMax;
        this.buildtype = buildtype;
        this.device = device;
    }
}
