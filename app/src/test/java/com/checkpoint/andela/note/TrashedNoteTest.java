package com.checkpoint.andela.note;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

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

/**
 * Created by andela on 14/02/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class TrashedNoteTest {
    private TrashedNote trashed;

    @Before
    public void setUp() throws Exception {
        trashed = Robolectric.setupActivity(TrashedNote.class);
    }


    @Test
    public void testOnCreate() throws Exception {
        View view = trashed.findViewById(R.id.drawer_layout);
        assertNotNull(view);
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {

    }

    @Test
    public void testOnNavigationItemSelected() throws Exception {
        MenuItem trashedNote = new RoboMenuItem(R.id.nav_notes);
        trashed.onNavigationItemSelected(trashedNote);

        ShadowActivity thisActivity = Shadows.shadowOf(trashed);
        Intent intent = thisActivity.peekNextStartedActivity();
        assertEquals(Application.class.getCanonicalName(), intent.getComponent().getClassName());
    }
}