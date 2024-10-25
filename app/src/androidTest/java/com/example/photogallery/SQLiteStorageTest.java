package com.example.photogallery;

import android.content.Context;

import androidx.test.InstrumentationRegistry;

import com.example.photogallery.db.SQLiteStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SQLiteStorageTest {
    Context appContext; SQLiteStorage ss;
    @Before
    public void initialization() {
        appContext = InstrumentationRegistry.getTargetContext();
        ss = new SQLiteStorage();
        int status = ss.init(appContext, "App.db");
        status = ss.addPhoto("cafe", "2018-08-25 13:20:21", "test_ cafe_2018-08-25 13:20:21_12345.jpg");
        status = ss.addPhoto("cafe", "2017-08-25 13:20:21", "test_cafe_2017-08-25 13:20:21_54321.jpg");
    }
    @Test
    public void TestSQLiteStorage() throws Exception {
        //Test keywords based search
        ArrayList<String> photos = ss.findPhotos("", "", "cafe");
        //Verify that two photos found with the specified keyword
        assertEquals(2, photos.size());
        assertEquals(true, photos.get(0).contains("cafe"));
        assertEquals(true, photos.get(1).contains("cafe"));
        //Test time based search
        //Initialize a time window around the time a Photo was taken
        Date startTimestamp = null, endTimestamp = null;
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startTimestamp = format.parse("2018-08-25 13:20:21");
            calendar.setTime(startTimestamp);
            calendar.add(Calendar.MINUTE, 30);
            endTimestamp = calendar.getTime();
        } catch (Exception ex) { }
        //Call the method
        photos = ss.findPhotos(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimestamp), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimestamp), "");
        //Verify that only one photo with the matching timestamp is found
        assertEquals(1, photos.size());
        assertEquals(true, photos.get(0).contains("2018-08-25 13:20:21"));
    }
    @After
    public void finalization() {
        int status = ss.deletePhoto("test_cafe_2018-08-25 13:20:21_12345.jpg");
        status = ss.deletePhoto("test_cafe_2017-08-25 13:20:21_54321.jpg");
    }

}
