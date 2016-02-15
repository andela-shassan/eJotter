package com.checkpoint.andela.note;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by andela on 11/02/2016.
 */
public class NewNoteTest extends ActivityInstrumentationTestCase2{
    public NewNoteTest() {
        super(NewNote.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testAddNote() {
        onView(withId(R.id.noteTitleText)).perform(typeText("Four"));
        onView(withId(R.id.noteBody)).perform(typeText("is awesome"));
        onView(withId(R.id.saveButton)).perform(click());
        onView(withText("Four")).perform(click()) /*check(matches(isDisplayed()))*/;
    }
}
