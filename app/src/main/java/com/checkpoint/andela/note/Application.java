package com.checkpoint.andela.note;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.checkpoint.andela.adapter.NoteModelAdapter;
import com.checkpoint.andela.db.NoteDB;
import com.checkpoint.andela.helpers.HelpFragment;
import com.checkpoint.andela.helpers.Launcher;
import com.checkpoint.andela.helpers.Settings;
import com.checkpoint.andela.model.NoteModel;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Application extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {
    private DrawerLayout drawer;
    private ArrayList<NoteModel> notes;
    private NoteModelAdapter adapter;
    private SQLiteDatabase db;
    private int currentNoteIndex;
    private ActionMode currentActionMode;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        initialize();
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        notes = new ArrayList<>();
        adapter = new NoteModelAdapter(this, notes);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        onLongClick(listView);

        populateNote(notes, "n");
        floatActionButton();
    }

    private void floatActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Launcher.destinationLauncher(Application.this, NewNote.class);
            }
        });
    }

    /**
     * In order to handle long click on an iten from the list, this method was provided
     * @param listView is the view that contains the item we are listening to.
     */
    private void onLongClick(ListView listView) {
        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentActionMode != null) {
                    return false;
                }
                currentNoteIndex = position;
                currentActionMode = startActionMode(actionCallBack);
                view.setSelected(true);
                return true;
            }
        });
    }

    /**
     * To fetch the data(Application) from the database and pass it to the view adapter.
     * @param notes are is the list of note objects that matches the sent query.
     * @param where is the condition that specifies which note to look for.
     */
    protected void populateNote(ArrayList<NoteModel> notes, String where){
        NoteDB dbHelper = new NoteDB(this);
        QueryResultIterable<NoteModel> noteModels = null;
        db = dbHelper.getWritableDatabase();
        Cursor allNotes = cupboard().withDatabase(db).query(NoteModel.class).withSelection("isTrashed = ?", where).getCursor();

        try {
            noteModels = cupboard().withCursor(allNotes).iterate(NoteModel.class);
            for (NoteModel noteModel : noteModels) {
                notes.add(0, noteModel);
            }
            setEmptyText(notes);
        } finally {
            noteModels.close();
        }
    }

    private void setEmptyText(ArrayList<NoteModel> notes) {
        if (notes.size() < 1) {
            View view = findViewById(R.id.empty_layout);
            view.setVisibility(view.VISIBLE);
        }
    }

    /**
     *To handle the longClick on note items from the list, this method was provided to to give users the freedom to
     * perform certain functions related to note management
     */
    private ActionMode.Callback actionCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_textview, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.editNotePopup:
                    editNote();
                    mode.finish();
                    return true;
                case R.id.deleteNotePopup:
                    trashNote(notes, adapter, "y", currentNoteIndex);
                    Toast.makeText(Application.this, "Note moved to trash", Toast.LENGTH_SHORT ).show();
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                case R.id.shareNotePopup:
                    sendNote();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null;
        }
    };

    /**
     * For the users to be able to send a particular Note to friends or family
     * using their favourite email client
     */
    private void sendNote() {
        NoteModel nm = notes.get(currentNoteIndex);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, "eJotter: " + nm.getTitle());
        email.putExtra(Intent.EXTRA_TEXT, nm.getContent());
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Send via..."));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
            exitApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Launcher.destinationLauncher(this, Settings.class);
                return true;
            case R.id.action_help:
                showHelpFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_trash:
                Launcher.destinationLauncher(this, TrashedNote.class);
                break;
            case R.id.nav_setting:
                Launcher.destinationLauncher(this, Settings.class);
                break;
            case R.id.help_drawer:
                showHelpFragment();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    protected void showHelpFragment() {
        FragmentManager fm = getSupportFragmentManager();
        HelpFragment help = new HelpFragment();
        help.show(fm, "fragment_help");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteModel note = adapter.getItem(position);
        Launcher.activityLauncher(this, ReadNote.class, note);
    }

    private void editNote() {
        NoteModel nm = notes.get(currentNoteIndex);
        NoteModel note = cupboard().withDatabase(db).get(NoteModel.class, nm.getId());
        Launcher.activityLauncher(Application.this, NewNote.class, note);
    }

    protected void trashNote(ArrayList<NoteModel> mNotes, NoteModelAdapter mAdapter, String trash, int currentNotePosition) {
        NoteModel nm = mNotes.get(currentNotePosition);
        mNotes.remove(currentNotePosition);
        NoteModel note = cupboard().withDatabase(db).get(NoteModel.class, nm.getId());
        note.setIsTrashed(trash);
        cupboard().withDatabase(db).put(note);
        mAdapter.notifyDataSetChanged();
        refresh();
    }

    protected void deleteNote(ArrayList<NoteModel> mNotes, NoteModelAdapter mAdapter, int index) {
        NoteModel nm = mNotes.get(index);
        cupboard().withDatabase(db).delete(NoteModel.class, nm.getId());
        mNotes.remove(index);
        mAdapter.notifyDataSetChanged();
        refresh();
    }

    protected void emptyTrash(String match) {
        cupboard().withDatabase(db).delete(NoteModel.class, "isTrashed = ?", match);
        refresh();
    }

    public void exitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void refresh() {
        Intent intent = getIntent();
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<NoteModel> result = filterNotes(notes, newText);
        adapter = new NoteModelAdapter(this, result);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return false;
    }

    private ArrayList<NoteModel> filterNotes(ArrayList<NoteModel> mainList, String query){
        query = query.toLowerCase();
        int size = mainList.size();
        NoteModel note;
        ArrayList<NoteModel> filteredList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            note = mainList.get(i);
            String title = note.getTitle().toLowerCase();
            String content = note.getContent().toLowerCase();

            if (title.contains(query)|| content.contains(query)){
                filteredList.add(note);
            }
        }
        return filteredList;
    }
}
