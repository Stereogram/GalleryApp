package com.example.unrea.galleryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.unrea.galleryapp", appContext.getPackageName());
    }


    @Test
    public void testDateFilter() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // Type text and then press the button.
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.dateText1)).perform(typeText("01/01/2012"), closeSoftKeyboard());
        onView(withId(R.id.dateText2)).perform(typeText("12/12/2015"), closeSoftKeyboard());
        onView(withId(R.id.dateButton)).perform(click());


        int numKids = 4;
        onView(withId(R.id.gridview)).check(matches(allOf(
                isDisplayed(),
                hasChildren(is(numKids))
        )));

        assertEquals("com.example.unrea.galleryapp", appContext.getPackageName());

    }

    @Test
    public void testLocationFilter() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // Type text and then press the button.
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.lat1)).perform(typeText("20"), closeSoftKeyboard());
        onView(withId(R.id.long1)).perform(typeText("20"), closeSoftKeyboard());
        onView(withId(R.id.lat2)).perform(typeText("30"), closeSoftKeyboard());
        onView(withId(R.id.long2)).perform(typeText("30"), closeSoftKeyboard());
        onView(withId(R.id.locationButton)).perform(click());

        int numKids = 3;
        onView(withId(R.id.gridview)).check(matches(allOf(
                isDisplayed(),
                hasChildren(is(numKids))
        )));

        assertEquals("com.example.unrea.galleryapp", appContext.getPackageName());

    }

    @Test
    public void testCaptionFilter() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // Type text and then press the button.
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.captionText)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.captionButton)).perform(click());

        int numKids = 1;
        onView(withId(R.id.gridview)).check(matches(allOf(
                isDisplayed(),
                hasChildren(is(numKids))
        )));

        assertEquals("com.example.unrea.galleryapp", appContext.getPackageName());

    }

    @Before
    public void setup() {
        for(int i = 0; i < 5; i++) {
            File file = new File ("/sdcard/Pictures/", "test " + i);
            if (file.exists ()) file.delete ();
            Bitmap icon = BitmapFactory.decodeResource(
                    InstrumentationRegistry.getTargetContext().getResources(),
                    R.mipmap.ic_launcher);
            try {
                FileOutputStream out = new FileOutputStream(file);
                icon.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                ExifInterface exif = new ExifInterface("/sdcard/Pictures/test"+ i +".jpg");
                exif.setAttribute(ExifInterface.TAG_DATETIME, "01/01/201" + i);
                exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, "test" + i);
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(10 * i));
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(10 * i));
                exif.saveAttributes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @After
    public void clean() {
        File dir = new File("/sdcard/Pictures/");
        for (File child : dir.listFiles()) {
            child.delete();
        }
    }

    public static Matcher<View> hasChildren(final Matcher<Integer> numChildrenMatcher) {
        return new TypeSafeMatcher<View>() {

            /**
             * matching with viewgroup.getChildCount()
             */
            @Override
            public boolean matchesSafely(View view) {
                return view instanceof ViewGroup && numChildrenMatcher.matches(((ViewGroup)view).getChildCount());
            }

            /**
             * gets the description
             */
            @Override
            public void describeTo(Description description) {
                description.appendText(" a view with # children is ");
                numChildrenMatcher.describeTo(description);
            }
        };
    }

}
