package com.example.photogallery;
import com.example.photogallery.db.FileStorage;
import android.content.Context;
import android.os.Build;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FileStorageTests {
    @Before  /*Initialization */
    public void grantPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                            + " android.permission.READ_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }
    @Test /*Unit Test for findPhotos method */
    public void findPhotosTest() throws Exception {
        // Using the App Context create an instance of the FileStorage
        Context appContext = InstrumentationRegistry.getTargetContext();
        FileStorage fs = new FileStorage(appContext);
        fs.createPhoto();

        //Test time based search
        //Initialize a time window around the time a Photo was taken
        Date startTimestamp = null, endTimestamp = null;
        try {
            Calendar calendar = Calendar.getInstance();
            // Set the startTimestamp to the current time minus 10 minutes
            calendar.add(Calendar.MINUTE, -1);
            startTimestamp = calendar.getTime();
            //DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            //startTimestamp = format.parse("2024-09-24-155005");
            calendar.setTime(startTimestamp);
            calendar.add(Calendar.MINUTE, 2);
            endTimestamp = calendar.getTime();

        } catch (Exception ex) { }

        //Call the method specifying the test time window.
        ArrayList<String> photos = fs.findPhotos(startTimestamp, endTimestamp, "");

        //Verify that only one photo was taken in the last minute
        assertEquals(1, photos.size());

        // Verify that the photo was taken today
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        assertEquals(true, photos.get(0).contains(today));
    }

}

