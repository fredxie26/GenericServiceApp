package com.example.photogallery;
//https://discuss.appium.io/t/click-on-capture-image-button-of-camera/3173/25
import android.content.Context;
import android.content.Intent;

import androidx.test.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AutoCameraTest {
    private static final String APP = "com.example.photogallery";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final int DEFAULT_TIMEOUT = 5000;
    private UiDevice mDevice;


    @Before  /* IInitialization*/
    public void startMainActivityFromHomeScreen() throws Exception{
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Create UI Automator Intent
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Launch the app
        context.startActivity(intent);
    }
    @Test /*  Take Photo Test */
    public void takePhoto() throws Exception {
        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        //Find and Click SNAP Button
        mDevice.findObject(new UiSelector().resourceId(APP + ":id/btnSnap")).click();

        //Click Camera Button of Android's Camera App
        Thread.sleep(3000);
        mDevice.executeShellCommand("input keyevent 27");
        Thread.sleep(3000);
        //mDevice.executeShellCommand("input keyevent 55");

        //Find and Click CONFIRM Button
        mDevice.findObject(new UiSelector().resourceId("com.android.camera2:id/done_button")).click();

        Thread.sleep(3000);
        /*mDevice.executeShellCommand("input keyevent 55");

        Thread.sleep(3000);
        mDevice.executeShellCommand("input keyevent 61");
        Thread.sleep(3000);
        mDevice.executeShellCommand("input keyevent 62");

        mDevice.findObject(new UiSelector().resourceId("//android.widget.Button[contains(@resource-id,‘okay’)]")).click();
        Find and Click OK Button of the Camera App*/
        //mDevice.findObject(new UiSelector().text("\u2713")).click();
        //mDevice.findObject(new UiSelector().text("OK")).click();
        //Thread.sleep(6000);
        //mDevice.executeShellCommand("input keyevent 27");
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        //Verify that the Photo is displayed with default caption
        UiObject etCaption = mDevice.findObject(new UiSelector().resourceId(APP + ":id/etCaption"));
        String defaultCaption = "caption";
        assertEquals(defaultCaption, etCaption.getText());

        //Verify that the Photo was taken recently
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE,(calendar.get(Calendar.MINUTE) - 50));
        Date startTimestamp = calendar.getTime();
        calendar.set(Calendar.MINUTE,(calendar.get(Calendar.MINUTE) + 50));
        Date endTimestamp = calendar.getTime();
        UiObject tvTimestamp = mDevice.findObject(new UiSelector().resourceId(APP + ":id/tvTimestamp"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date photoDate = format.parse(tvTimestamp.getText());
        assertEquals(true,photoDate.after(startTimestamp) && photoDate.before(endTimestamp) );

        //Go back to the Home Screen
        mDevice.pressHome();
    }
}
