package com.checkpoint.andela.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.checkpoint.andela.helpers.Launcher;
import com.checkpoint.andela.model.NoteModel;

public class ReadNote extends AppCompatActivity {
    private NoteModel note;
    private TextView content;
    private long id;
    private String head;
    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        initialize();
        getNote();
    }

    private void initialize() {
        content = (TextView) findViewById(R.id.theContent);
        content.setMovementMethod(new ScrollingMovementMethod());

        Toolbar toolbar = (Toolbar) findViewById(R.id.read_note_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Launcher.activityLauncher(ReadNote.this, NewNote.class, note);
            }
        });
    }

    /**
     * Get note as a parcelable object and decode it into human readable
     */
    private void getNote() {
        Intent intent = getIntent();
        if(intent.getParcelableExtra("UPDATE") != null){
            note = intent.getParcelableExtra("UPDATE");
            setNoteView();
            setTitle(note.getTitle());
        }
    }

    private void setNoteView() {
        content.setText(note.getContent());
        this.id = note._id;
        this.head = note.getDate();
        this.body = note.getContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Launcher.destinationLauncher(this, Application.class);
    }

}
