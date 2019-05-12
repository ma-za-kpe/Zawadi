







package com.maku.zawadi;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class) // allows our code to be run natively in the JVM instead of on an Android device
public class SplashActivityTest {

    //configure to know which activity to run before testing
    private Splash splash;

    @Before
    public void setup(){
        splash = Robolectric.setupActivity(Splash.class);
    }

    @Test
    public void test(){
         splash = new Splash();

        assertFalse(splash.isFinishing());

        splash.runLauncher();
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        assertTrue(splash.isFinishing());
    }

}