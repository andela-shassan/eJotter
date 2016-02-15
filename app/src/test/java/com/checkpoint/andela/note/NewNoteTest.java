package com.checkpoint.andela.note;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import com.checkpoint.andela.helpers.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by andela on 05/02/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class NewNoteTest {
    private NewNote activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(NewNote.class);
    }

    @Test
    public void testOnCreate() throws Exception {
        View view = activity.findViewById(R.id.new_note);
        assertNotNull(view);
    }

    @Test
    public void testOnCreateOptionsMenu() throws Exception {
        MenuItem setting = new RoboMenuItem(R.menu.new_note);
        assertNotNull(setting);
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertTrue(activity.onOptionsItemSelected(menuItem));

        ShadowActivity thisActivity = Shadows.shadowOf(activity);
        Intent intent = thisActivity.peekNextStartedActivity();
        assertEquals(Settings.class.getCanonicalName(), intent.getComponent().getClassName());
    }

    @Test
    public void testAutoSave() throws Exception {
        activity.autoSave();
        assertTrue(!activity.isFinishing());
    }
}