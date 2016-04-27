package com.checkpoint.andela.note;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
        onView(withText("Google")).check(doesNotExist());
        onView(withText("Semiu")).check((doesNotExist()));
    }

    @Test
    public void testClickNewNoteButton() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click());
    }

    @Test
    public void testAddNote() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click());
        onView(withId(R.id.noteTitleText)).perform(typeText("ANDELA3"));
        onView(withId(R.id.noteBody)).perform(typeText("is awesome"));
        onView(withId(R.id.saveButton)).perform(click());
        onView(withText("ANDELA3")).check(matches(isDisplayed()));
    }

    @Test
    public void testLongClick() {
        Espresso.onView(withText("Four")).perform(longClick());
        onView(withId(R.id.deleteNotePopup)).perform(click());
    }

    @Test
    public void testClickOnNoteItem() {
        Espresso.onView(withText("ANDELA3")).perform(click());
        onView(withId(R.id.theContent)).check(ViewAssertions.matches(withText("is awesome")));
    }

}
