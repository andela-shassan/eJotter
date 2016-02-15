package com.checkpoint.andela.note;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;

import com.checkpoint.andela.model.NoteModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by andela on 05/02/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ApplicationTest {
    private Application application;

    @Before
    public void setUp() throws Exception {
        application = Robolectric.setupActivity(Application.class);
    }

    @Test
    public void testOnCreateView() throws Exception {
        View view = application.findViewById(R.id.drawer_layout);
        assertNotNull(view);
    }

    @Test
    public void testPopulateNote() throws Exception {
        ArrayList<NoteModel> testList = new ArrayList<>();
        application.populateNote(testList, "n");
        assertNotNull(testList);
    }

    @Test
    public void testDrawableIcons() throws Exception {
        Drawable checkmark = application.getResources().getDrawable(R.drawable.checkmarkw);
        assertNotNull(checkmark);

        Drawable trash = application.getResources().getDrawable(R.drawable.trash_blue);
        assertNotNull(trash);
    }

    @Test
    public void testOnCreateOptionsMenu() throws Exception {
        MenuItem setting = new RoboMenuItem(R.menu.notes);
        assertNotNull(setting);

        MenuItem help = new RoboMenuItem(R.menu.notes);
        assertNotNull(help);
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {
        Application activity = Robolectric.setupActivity(Application.class);
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertEquals(activity.onOptionsItemSelected(menuItem),true );
    }

    @Test
    public void testOnNavigationItemSelected() throws Exception {
        Application activity = Robolectric.setupActivity(Application.class);
        MenuItem menuItem = new RoboMenuItem(R.id.nav_trash);
        assertEquals(activity.onNavigationItemSelected(menuItem),true );
    }

    @Test
    public void testLaunchTrashedActivity() throws Exception {
        MenuItem trashedNote = new RoboMenuItem(R.id.nav_trash);
        application.onNavigationItemSelected(trashedNote);

        ShadowActivity thisActivity = Shadows.shadowOf(application);
        Intent intent = thisActivity.peekNextStartedActivity();
        assertEquals(TrashedNote.class.getCanonicalName(), intent.getComponent().getClassName());
    }

    @Test
    public void testOnFloatActionButtonClick() throws Exception {
        FloatingActionButton fab = (FloatingActionButton) application.findViewById(R.id.fab);
        fab.performClick();
        ShadowActivity thisActivity = Shadows.shadowOf(application);
        Intent intent = thisActivity.peekNextStartedActivity();
        assertEquals(NewNote.class.getCanonicalName(), intent.getComponent().getClassName());
    }

}