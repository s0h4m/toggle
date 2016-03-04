package cc.soham.toggle;

import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import cc.soham.toggle.objects.Device;
import cc.soham.toggle.objects.Rule;
import cc.soham.toggle.objects.Value;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for the Rules
 */
@SmallTest
@RunWith(PowerMockRunner.class)
@PrepareForTest({RuleMatcher.class, System.class, TextUtils.class})
public class RuleMatcherTests {
    @Before
    public void setup() {
    }

    @After
    public void tearDown() {

    }

    // matchApilevelMax

    @Test
    public void matchApilevelMax_lowerActualVersion_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(22);
        boolean result = RuleMatcher.matchApilevelMax(23);
        assertThat(result).isTrue();
    }

    @Test
    public void matchApilevelMax_sameVersion_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(23);
        boolean result = RuleMatcher.matchApilevelMax(23);
        assertThat(result).isTrue();
    }

    @Test
    public void matchApilevelMax_higherActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(22);
        boolean result = RuleMatcher.matchApilevelMax(21);
        assertThat(result).isFalse();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchApilevelMax_default_returnsTrue() {
        boolean result = RuleMatcher.matchApilevelMax(null);
        assertThat(result).isTrue();
    }

    // matchApilevelMin

    @Test
    public void matchApilevelMin_lowerActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(22);
        boolean result = RuleMatcher.matchApilevelMin(23);
        assertThat(result).isFalse();
    }

    @Test
    public void matchApilevelMin_sameVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(23);
        boolean result = RuleMatcher.matchApilevelMin(23);
        assertThat(result).isTrue();
    }

    @Test
    public void matchApilevelMin_higherActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(23);
        boolean result = RuleMatcher.matchApilevelMin(23);
        assertThat(result).isTrue();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchApilevelMin_default_returnsTrue() {
        boolean result = RuleMatcher.matchApilevelMin(null);
        assertThat(result).isTrue();
    }

    // matchAppversionMax

    @Test
    public void matchAppVersionMax_lowerActualVersion_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(9);
        boolean result = RuleMatcher.matchAppversionMax(10);
        assertThat(result).isTrue();
    }

    @Test
    public void matchAppVersionMax_sameVersion_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(10);
        boolean result = RuleMatcher.matchAppversionMax(10);
        assertThat(result).isTrue();
    }

    @Test
    public void matchAppVersionMax_higherActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(11);
        boolean result = RuleMatcher.matchAppversionMax(10);
        assertThat(result).isFalse();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchAppVersion_default_returnsTrue() {
        boolean result = RuleMatcher.matchAppversionMax(null);
        assertThat(result).isTrue();
    }

    // matchAppversionMin

    @Test
    public void matchAppVersionMin_lowerActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(9);
        boolean result = RuleMatcher.matchAppversionMin(10);
        assertThat(result).isFalse();
    }

    @Test
    public void matchAppVersionMin_sameVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(10);
        boolean result = RuleMatcher.matchAppversionMin(10);
        assertThat(result).isTrue();
    }

    @Test
    public void matchAppVersionMin_higherActualVersion_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(11);
        boolean result = RuleMatcher.matchAppversionMin(10);
        assertThat(result).isTrue();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchAppVersionMin_default_returnsTrue() {
        boolean result = RuleMatcher.matchAppversionMin(null);
        assertThat(result).isTrue();
    }

    @Test
    public void matchBuildType_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildType()).thenReturn("debug");
        boolean result = RuleMatcher.matchBuildType("debug");
        assertThat(result).isTrue();
    }

    @Test
    public void matchBuildType_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildType()).thenReturn("debug");
        boolean result = RuleMatcher.matchBuildType("release");
        assertThat(result).isFalse();
    }

    // matchDateMax

    @Test
    public void matchDateMax_lowerActualVersion_returnsTrue() {
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        boolean result = RuleMatcher.matchDateMax(1453196880000L);
        assertThat(result).isTrue();
    }

    @Test
    public void matchDateMax_sameVersion_returnsTrue() {
        PowerMockito.spy(System.class);
        initalizeCurrentTimeLate();
        boolean result = RuleMatcher.matchDateMax(1453196880000L);
        assertThat(result).isTrue();
    }

    @Test
    public void matchDateMax_higherActualVersion_returnsFalse() {
        PowerMockito.spy(System.class);
        Mockito.when(System.currentTimeMillis()).thenReturn(1453199990000L);
        boolean result = RuleMatcher.matchDateMax(1453196880000L);
        assertThat(result).isFalse();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchDateMax_default_returnsTrue() {
        boolean result = RuleMatcher.matchDateMax(null);
        assertThat(result).isTrue();
    }

    // matchApilevelMin

    @Test
    public void matchDatelMin_lowerActualVersion_returnsFalse() {
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        boolean result = RuleMatcher.matchDateMin(1453196880000L);
        assertThat(result).isFalse();
    }

    @Test
    public void matchDateMin_sameVersion_returnsFalse() {
        PowerMockito.spy(System.class);
        initalizeCurrentTimeLate();
        boolean result = RuleMatcher.matchDateMin(1453196880000L);
        assertThat(result).isTrue();
    }

    private void initalizeCurrentTimeLate() {
        Mockito.when(System.currentTimeMillis()).thenReturn(1453196880000L);
    }

    @Test
    public void matchDateMin_higherActualVersion_returnsFalse() {
        PowerMockito.spy(System.class);
        Mockito.when(System.currentTimeMillis()).thenReturn(1453199990000L);
        boolean result = RuleMatcher.matchDateMin(1453196880000L);
        assertThat(result).isTrue();
    }

    /**
     * This is in this is not included in {@link cc.soham.toggle.objects.Value} in the
     * processed {@link cc.soham.toggle.objects.Rule}
     */
    @Test
    public void matchDateMin_default_returnsTrue() {
        boolean result = RuleMatcher.matchDateMin(null);
        assertThat(result).isTrue();
    }

    // device match

    @Test
    public void matchDevice_onlyModel_matches_returnsTrue() {
        Device device = new Device(null, "mi3");
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getModel()).thenReturn("mi3");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isTrue();
    }

    @Test
    public void matchDevice_onlyModel_doesNotMatch_returnsFalse() {
        Device device = new Device(null, "mi3");
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getModel()).thenReturn("galaxys4");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isFalse();
    }

    @Test
    public void matchDevice_onlyManf_matches_returnsTrue() {
        Device device = new Device("xiaomi", null);
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("xiaomi");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isTrue();
    }

    @Test
    public void matchDevice_onlyManf_doesNotMatch_returnsFalse() {
        Device device = new Device("xiaomi", null);
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("samsung");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isFalse();
    }

    @Test
    public void matchDevice_both_matches_returnsTrue() {
        Device device = new Device("xiaomi", "mi3");
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("xiaomi");
        Mockito.when(RuleMatcher.getModel()).thenReturn("mi3");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isTrue();
    }

    @Test
    public void matchDevice_both_manfDoesNotMatch_returnsFalse() {
        Device device = new Device("xiaomi", "mi3");
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("samsung");
        Mockito.when(RuleMatcher.getModel()).thenReturn("mi3");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isFalse();
    }

    @Test
    public void matchDevice_both_modelDoesNotMatch_returnsFalse() {
        Device device = new Device("xiaomi", "mi3");
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("xiaomi");
        Mockito.when(RuleMatcher.getModel()).thenReturn("mi4");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isFalse();
    }

    @Test
    public void matchDevice_both_bothDoNotMatch_returnsFalse() {
        Device device = new Device("xiaomi", "mi3");
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("samsung");
        Mockito.when(RuleMatcher.getModel()).thenReturn("galaxys4");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        boolean result = RuleMatcher.matchDevice(new ArrayList<Device>(deviceList));
        assertThat(result).isFalse();
    }

    @Test
    public void matchRule_apiLevelCheck_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(22);
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, null, null, null, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isTrue();
    }

    @Test
    public void matchRule_apiLevelCheck_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(22);
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(23, null, null, null, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isFalse();
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        initializeBuildVersion18();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, null, null, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isTrue();
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        initializeBuildVersion18();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 16, null, null, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isFalse();
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_appversionMixMaxCheck_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        intializeBuildVersion18AndVersionCode100();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 90, 110, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isTrue();
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_appversionMixMaxCheck_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        intializeBuildVersion18AndVersionCode100();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 110, 120, null, null, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isFalse();
    }

    private void intializeBuildVersion18AndVersionCode100() {
        initializeBuildVersion18();
        initializeVersionCode100();
    }

    private void initializeBuildVersion18() {
        Mockito.when(RuleMatcher.getBuildVersion()).thenReturn(18);
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_appversionMixMaxCheck_dateMixMaxCheck_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        intializeBuildVersion18AndVersionCode100();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 90, 110, 1450000000000L, 1459990000000L, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isTrue();
    }

    @Test
    public void matchRule_apiLevelMixMaxCheck_appversionMixMaxCheck_dateMixMaxCheck_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        intializeBuildVersion18AndVersionCode100();
        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 90, 110, 1453199990000L, 1453199999999L, null, null));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isFalse();
    }

    @Test
    public void matchRule_allCheck_correct_returnsTrue() {
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        intializeBuildVersion18AndVersionCode100();

        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("xiaomi");
        Mockito.when(RuleMatcher.getModel()).thenReturn("mi3");

        // just for xiaomi devices
        Device device = new Device("xiaomi", null);
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);

        Mockito.when(RuleMatcher.getBuildType()).thenReturn("release");

        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 90, 110, 1450000000000L, 1459990000000L, "release", deviceList));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isTrue();
    }

    private void initializeVersionCode100() {
        Mockito.when(RuleMatcher.getVersionCode()).thenReturn(100);
    }

    @Test
    public void matchRule_allCheck_incorrect_returnsFalse() {
        PowerMockito.spy(RuleMatcher.class);
        PowerMockito.spy(System.class);
        initializeCurrentTimeBasic();
        intializeBuildVersion18AndVersionCode100();

        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        Mockito.when(RuleMatcher.getManufacturer()).thenReturn("samsung");
        Mockito.when(RuleMatcher.getModel()).thenReturn("galaxys4");

        Device device = new Device("xiaomi", "mi3");
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);

        Mockito.when(RuleMatcher.getBuildType()).thenReturn("release");

        Rule rule = new Rule(Toggle.DISABLED, null, new Value(14, 22, 90, 110, 1450000000000L, 1459990000000L, "release", deviceList));
        boolean result = RuleMatcher.matchRule(rule);
        assertThat(result).isFalse();
    }

    private void initializeCurrentTimeBasic() {
        Mockito.when(System.currentTimeMillis()).thenReturn(1453190000000L);
    }
}
