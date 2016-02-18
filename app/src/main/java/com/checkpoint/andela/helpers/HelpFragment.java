package com.checkpoint.andela.helpers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.checkpoint.andela.note.R;

public class HelpFragment extends DialogFragment {

    public HelpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container);
        Button b = (Button) v.findViewById(R.id.dismiss_help);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView helpText1 = (TextView) view.findViewById(R.id.help_view1);
        TextView helpText2 = (TextView) view.findViewById(R.id.help_view2);
        TextView helpText3 = (TextView) view.findViewById(R.id.help_view3);
        TextView helpText4 = (TextView) view.findViewById(R.id.help_view4);
        TextView helpText5 = (TextView) view.findViewById(R.id.help_view5);
        TextView helpText6 = (TextView) view.findViewById(R.id.help_view6);
        TextView helpText7 = (TextView) view.findViewById(R.id.help_view7);
        helpText1.setText(R.string.help1);
        helpText2.setText(R.string.help2);
        helpText3.setText(R.string.help3);
        helpText4.setText(R.string.help4);
        helpText5.setText(R.string.help5);
        helpText6.setText(R.string.help6);
        helpText7.setText(R.string.help7);
        getDialog().setTitle("Help?");
    }
}