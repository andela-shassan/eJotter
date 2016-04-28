package com.checkpoint.andela.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.checkpoint.andela.adapter.NoteModelAdapter;
import com.checkpoint.andela.helpers.Launcher;
import com.checkpoint.andela.helpers.Settings;
import com.checkpoint.andela.model.NoteModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TrashedNote extends Application {
    private DrawerLayout drawer;
    private ArrayList<NoteModel> trashed;
    private NoteModelAdapter adapter;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        trashed = new ArrayList<>();
        adapter = new NoteModelAdapter(this, trashed);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        populateNote(trashed, "y");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Launcher.destinationLauncher(this, Application.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trashed_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.empty_trashed) {
            TrashDialogue td = new TrashDialogue();
            FragmentManager fm = getSupportFragmentManager();
            td.show(fm, "Empty Trash");
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_notes:
                Launcher.destinationLauncher(this, Application.class);
                return true;
            case R.id.nav_setting:
                Launcher.destinationLauncher(this, Settings.class);
                return true;
            case R.id.help_drawer:
                showHelpFragment();
                return true;
            default:
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.trashed_popup);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_restore_note:
                        trashNote(trashed, adapter, "n", position);
                        Toast.makeText(TrashedNote.this, "Note restored successfully", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_delete_note:
                        deleteNote(trashed, adapter, position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        if (showIcons(popup)) {
            return;
        }
        popup.show();
    }

    private boolean showIcons(PopupMenu popup) {
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w("POPUP", "error forcing menu icons to show", e);
            popup.show();
            return true;
        }
        return false;
    }

    // Reaffirmation of Trash emptying.
    public static class TrashDialogue extends DialogFragment {
        private SQLiteDatabase db;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Empty the trash? This cannot be undo!")
                    .setPositiveButton("Empty it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            emptyTrash("y");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            getActivity().recreate();
            super.onDismiss(dialog);
        }
    }

}
