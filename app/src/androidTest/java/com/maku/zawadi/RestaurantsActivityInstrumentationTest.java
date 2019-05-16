package com.maku.zawadi;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

public class RestaurantsActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<RestaurantsActivity> activityTestRule =
            new ActivityTestRule<>(RestaurantsActivity.class);

}
