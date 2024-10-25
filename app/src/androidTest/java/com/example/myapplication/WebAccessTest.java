package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.os.Environment;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.net.WebAccess;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class WebAccessTest {
    @Test
    public void testPhotoUpload() {
        //Run MainActivity and snap a photo before running this test
        //create WebAccess instance and specify the server end-point
        //Tomcat server is running locally on port 8081
        WebAccess wa = new WebAccess("10.0.2.2:8081/upload/upload");
        //get list of photos in Pictures folder to select one to upload
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.myapplication/files/Pictures");
        File[] fList = file.listFiles();
        if (fList == null)
            return;
        //get all the photos already uploaded to the remote site
        String[] photos = wa.listAllPhotos();
        if (photos != null) {
            //check if the selected photo already exists on the remote site
            assertEquals(false, Arrays.asList(photos).contains(fList[0].getName()));
        }
        // upload the selected photo
        String response = wa.uploadPhoto(fList[0]);
        //confirm if the photo uploaded successfully
        photos = wa.listAllPhotos();
        assertEquals(true, Arrays.asList(photos).contains(fList[0].getName()));
    }
}


