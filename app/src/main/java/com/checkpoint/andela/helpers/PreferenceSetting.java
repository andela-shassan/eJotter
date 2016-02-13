package com.checkpoint.andela.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.checkpoint.andela.note.R;

/**
 * Created by andela on 12/02/2016.
 */
public class PreferenceSetting extends DialogPreference implements Preference.OnPreferenceChangeListener, NumberPicker.OnValueChangeListener {
    public static final String DEFAULT_VALUE = "5";

    private NumberPicker numberPicker;

    public PreferenceSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.number_picker);
        setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        int sec = numberPicker.getValue();
        Log.e("Default", String.valueOf(sec));
        if (positiveResult){
            String inputValue = String.valueOf(sec);
            if (callChangeListener(inputValue)){
                persistString(inputValue);
                setSummary(inputValue);
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            setSummary(retreiveSettings());
        }
        else {
            persistString(defaultValue.toString());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(newValue.toString());
        return true;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {}

    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    protected Object onGetDefaultValue(TypedArray array, int index) {
        return array.getString(index);
    }

    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();
        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(5);
        return view;
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    private String retreiveSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("preference_key", DEFAULT_VALUE);
    }
}