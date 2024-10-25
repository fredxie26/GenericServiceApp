package com.example.photogallery;

import org.junit.Rule;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.test.ext.junit.rules.ActivityScenarioRule;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class UITest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test  /* Find Photos Test*/
    public void timeBasedSearch() throws Exception {
        // Take a photo with MainActivity and update the below dateTimes
        // Initialize a test time window assuming that one photo was taken at the time
        String startTime = "2024-10-02 12:05:24";
        String captionTime = "2024-10-02-120524";

        Date startTimestamp = null, endTimestamp = null;
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //The following test data assumes that only one Photo was taken on 2021-02-27
            //
            startTimestamp = format.parse(startTime);
            calendar.setTime(startTimestamp);
            startTimestamp = calendar.getTime();
            calendar.add(Calendar.MINUTE, 1);
            endTimestamp = calendar.getTime();

        } catch (Exception ex) { }

        //Find and Click the Search Button
        onView(withId(R.id.btnSearch)).perform(click());

        //Find From and To fields in the Search layout and fill these with the above test data
        String from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(startTimestamp);
        String to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(endTimestamp);
        onView(withId(R.id.etFromDateTime)).perform(replaceText(from), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(replaceText(to), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(replaceText(""), closeSoftKeyboard());

        //Find and Click the GO button on the Search View
        onView(withId(R.id.go)).perform(click());

        //Verify that the timestamp of the found Image matches the Expected value
        onView(withId(R.id.tvTimestamp)).check(matches(withText(captionTime)));
    }
}