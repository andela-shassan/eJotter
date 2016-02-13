package com.checkpoint.andela.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.checkpoint.andela.model.NoteModel;

/**
 * Created by andela on 08/02/2016.
 */
public class Launcher extends AppCompatActivity {

    public static void activityLauncher(Context context, Class<?> destination, NoteModel note) {
        Intent intent = new Intent(context, destination);
        intent.putExtra("UPDATE", note);
        context.startActivity(intent);
    }

    public static void destinationLauncher(Context context, Class<?> destination) {
        Intent intent = new Intent(context, destination);
        context.startActivity(intent);
    }

}
