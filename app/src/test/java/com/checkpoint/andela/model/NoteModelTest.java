package com.checkpoint.andela.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by andela on 14/02/2016.
 */

/*@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)*/
public class NoteModelTest {
    private NoteModel note; /*= new NoteModel();*/

    @Before
    public void setUp() throws Exception {
        note = new NoteModel();
    }

    @Test
    public void testGetTitle() throws Exception {
        assertNull(note.getTitle());
    }

    @Test
    public void testSetTitle() throws Exception {
        note.setTitle("andela");
        assertNotNull(note.getTitle());
        assertEquals(note.getTitle(), "andela");
    }

    @Test
    public void testGetContent() throws Exception {
        assertNull(note.getContent());
    }

    @Test
    public void testSetContent() throws Exception {
        assertNull(note.getContent());
        note.setContent("andela is fun");
        assertNotNull(note.getContent());
        assertTrue(note.getContent().equals("andela is fun"));
    }

    @Test
    public void testSetDateTime() throws Exception {
        assertTrue(note.setDateTime().contains("2016"));
    }

    @Test
    public void testGetDate() throws Exception {
        assertTrue(note.getDate().contains("2016"));
    }

    @Test
    public void testDescribeContents() throws Exception {
        assertTrue(note.describeContents() == 0);
    }

}