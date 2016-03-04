package cc.soham.togglesample;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.soham.toggle.Toggle;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anyOf;

/**
 * Checks the Retrofit calls in the sample
 */
@RunWith(AndroidJUnit4.class)
public class ToggleRetrofitTests {
    @Rule
    public ActivityTestRule<SampleRetrofitActivity> activityTestRule = new ActivityTestRule<>(SampleRetrofitActivity.class);

    public ProgressBarIdlingResource progressBarIdlingResource;

    @Before
    public void setup() {
        progressBarIdlingResource = new ProgressBarIdlingResource(activityTestRule.getActivity());
    }

    @After
    public void tearDown() {
        Espresso.unregisterIdlingResources(progressBarIdlingResource);
    }

    @Test
    @MediumTest
    public void toggle_network_setConfig() {
        // make sure the config is not loaded from memory
        Toggle.storeConfigInMem(null);
        // perform the button click
        onView(withId(R.id.activity_sample_set_config)).perform(click());
        // register the idling resource so that we can know when the config is done
        Espresso.registerIdlingResources(progressBarIdlingResource);
        // check
        assertThat(Toggle.getConfig()).isNotNull();
        assertThat(Toggle.getConfig().name).isNotNull();
        assertThat(Toggle.getConfig().features).isNotNull();
    }

    @Test
    @MediumTest
    public void toggle_network_check() {
        // make sure the config is not loaded from memory
        Toggle.storeConfigInMem(null);
        // perform the button click
        onView(withId(R.id.activity_sample_check)).perform(click());
        // register the idling resource so that we can know when the config is done
        Espresso.registerIdlingResources(progressBarIdlingResource);
        // check
        onView(withId(R.id.activity_sample_feature)).check(matches(anyOf(withText(Toggle.ENABLED), withText(Toggle.DISABLED))));
        onView(withId(R.id.activity_sample_feature_feature_metadata)).check(matches(withText(any(String.class))));
        onView(withId(R.id.activity_sample_feature_rule_metadata)).check(matches(withText(any(String.class))));
        onView(withId(R.id.activity_sample_feature_cached)).check(matches(withText("Cached: true")));
    }
}
