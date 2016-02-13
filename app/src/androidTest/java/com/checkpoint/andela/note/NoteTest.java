package com.checkpoint.andela.note;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by andela on 11/02/2016.
 */
public class NoteTest extends ActivityInstrumentationTestCase2 {
    public NoteTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void testDisplayItem() {
        Espresso.onView(withText("Google")).check(doesNotExist());
        Espresso.onView(withText("Semiu")).check((doesNotExist()));
    }

    @Test
    public void testLongClick() {
        Espresso.onView(withText("Two")).perform(longClick());
    }

    @Test
    public void testClickOnNoteItem() {
        Espresso.onView(withText("ANDELA")).perform(click());
        onView(withId(R.id.theContent)).check(ViewAssertions.matches(withText("is awesome")));
    }

    @Test
    public void testClickNewNoteButton() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click());
    }

}



























































































/*public void testOpenNewNoteOnCLick() {

        ShadowActivity homedashboardShadowable = Shadows.shadowOf(homeDashboardActivity);
        Intent actualStartedIntent = homedashboardShadowable.getNextStartedActivity();
        assertTrue(actualStartedIntent.filterEquals(expectedIntent));
    }*/