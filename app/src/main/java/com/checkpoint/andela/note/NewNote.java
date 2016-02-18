package com.checkpoint.andela.note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.checkpoint.andela.db.NoteDB;
import com.checkpoint.andela.helpers.Launcher;
import com.checkpoint.andela.helpers.Settings;
import com.checkpoint.andela.model.NoteModel;

import static java.lang.Integer.parseInt;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * This class was provided to handle the responsibility of creating a new note
 * and save it into the database.
 * Also, to update an existing note.
 */

public class NewNote extends AppCompatActivity {
    private SQLiteDatabase db;
    private NoteModel newNote;
    private Toolbar toolbar;

    public NewNote() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        initialize();
        autoSave();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        NoteDB dbHelper = new NoteDB(this);
        db = dbHelper.getWritableDatabase();
        newNote = new NoteModel();

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNote();
                Launcher.destinationLauncher(NewNote.this, Application.class);
            }
        });
        updateNotes();
    }

    /**
     * The objective of this method is to get data entered by the user and set each value accordingly
     */
    private void setNote() {
        EditText title = (EditText) findViewById(R.id.noteTitleText);
        EditText content = (EditText) findViewById(R.id.noteBody);
        String noteTitle = title.getText().toString().trim();
        String noteContent = content.getText().toString().trim();

        if (noteTitle.matches("") && noteContent.matches("")) {
            return;
        } else if (noteTitle.matches("") && noteContent.contains(" ")) {
            newNote.setTitle(noteContent.substring(0, noteContent.indexOf(" ")));
        } else {
            newNote.setTitle(noteTitle);
        }
        newNote.setContent(noteContent);
        saveNote(newNote);
    }

    /**
     * This method is used to insert notes into the database
     * @param noteModel is the note object to be saved.
     * @return
     */
    private boolean saveNote(NoteModel noteModel) {
        cupboard().withDatabase(db).put(noteModel);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Launcher.destinationLauncher(this, Settings.class);
                return true;
            case R.id.action_save:
                setNote();
                Launcher.destinationLauncher(this, Application.class);
                return true;
            case R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 'This is the method to get the note to be updated. It it necessary to get the note
     * as a Parcelable object and set it on the edit text.
     */
    protected void updateNotes() {
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            newNote = intent.getParcelableExtra("UPDATE");
            this.setTitle("Update Note");
            EditText uTitle = (EditText) findViewById(R.id.noteTitleText);
            EditText uContent = (EditText) findViewById(R.id.noteBody);
            uTitle.setText(newNote.getTitle());
            uContent.setText(newNote.getContent());
        }
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        Launcher.destinationLauncher(this, Application.class);
    }

    private int getUserTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String time = preferences.getString("preference_key", "5");
        return parseInt(time);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setNote();
            handler.postDelayed(this, 1000 * getUserTime());
        }
    };

    public void autoSave() {
        final EditText nTitle = (EditText) findViewById(R.id.noteTitleText);
        final EditText nContent = (EditText) findViewById(R.id.noteBody);
        nTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(nTitle.hasFocus() || nContent.hasFocus()){
                    runnable.run();
                }
                else {
                    handler.removeCallbacks(runnable);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}