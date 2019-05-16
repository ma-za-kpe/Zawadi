package com.maku.zawadi;

import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) // allows our code to be run natively in the JVM instead of on an Android device
public class MainActivityTest {

    //configure to know which activity to run before testing
    private MainActivity mainActivity ;

    @Test
    public void validateConfirmTextViewContent(){
        TextView mmobile_numberTextView = mainActivity.findViewById(R.id.mobile_number);
        assertThat(mmobile_numberTextView.getText().toString(), equalTo(mmobile_numberTextView.getText().toString()));

    }

}
