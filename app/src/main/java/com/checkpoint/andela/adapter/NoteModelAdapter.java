package com.checkpoint.andela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.checkpoint.andela.model.NoteModel;
import com.checkpoint.andela.note.R;

import java.util.ArrayList;

/**
 * Created by andela on 03/02/2016.
 */
public class NoteModelAdapter extends ArrayAdapter<NoteModel> implements PopupMenu.OnMenuItemClickListener {

    public NoteModelAdapter(Context context, ArrayList<NoteModel> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        NoteModel noteModel = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.note_rows, viewGroup, false);
        }

        TextView title = (TextView) view.findViewById(R.id.aNoteTitle);
        TextView content = (TextView) view.findViewById(R.id.aNoteContent);
        TextView time = (TextView) view.findViewById(R.id.aNoteTime);

        title.setText(noteModel.getTitle());
        content.setText(noteModel.getContent());
        time.setText(noteModel.getDate());

       return view;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
