package com.checkpoint.andela.note;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by andela on 05/02/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ReadNoteTest {
    private ReadNote activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(ReadNote.class);
    }

    @Test
    public void testOnCreate() throws Exception {
        View view = activity.findViewById(R.id.read_note);
        assertNotNull(view);
    }

    @Test
    public void testEditNote() throws Exception {
        FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        fab.performClick();
        ShadowActivity thisActivity = Shadows.shadowOf(activity);
        Intent intent = thisActivity.peekNextStartedActivity();
        assertEquals(NewNote.class.getCanonicalName(), intent.getComponent().getClassName());
    }
}