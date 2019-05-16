package com.maku.zawadi;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) // allows our code to be run natively in the JVM instead of on an Android device
public class LoginActivityTest {

    //configure to know which activity to run before testing
    private LoginActivity loginActivity;

    @Before
    public void setup(){
        loginActivity = Robolectric.setupActivity(LoginActivity.class);
    }

    @Test
    public void validateWelcomTextViewContent(){
        TextView wolcomeTextView = loginActivity.findViewById(R.id.wolcomeTextView);
        assertThat(wolcomeTextView.getText().toString(), equalTo("Welcome!"));

    }

    @Test
    public void validateConfirmTextViewContent(){
        TextView memailTextView = loginActivity.findViewById(R.id.emailTextView);
        assertThat(memailTextView.getText().toString(), equalTo("You will get an SMS to confirm your number"));

    }

    //testing the login button
    @Test
    public void buttonClickShouldStartNewActivity() throws Exception
    {
        Button button = (Button) loginActivity.findViewById( R.id.loginButton );
        button.performClick();
        Intent intent = Shadows.shadowOf(loginActivity).peekNextStartedActivity();
        assertEquals(MainActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }

}
