package com.checkpoint.andela.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.checkpoint.andela.note.R;

import static java.lang.Integer.parseInt;

/**
 * Created by andela on 12/02/2016.
 */
public class PreferenceSetting extends DialogPreference implements
        Preference.OnPreferenceChangeListener, NumberPicker.OnValueChangeListener {
    public final String DEFAULT_VALUE = "5";
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
        if (positiveResult) {
            String inputValue = String.valueOf(sec);
            if (callChangeListener(inputValue)) {
                persistString(inputValue);
                setSummary(inputValue + " Seconds");
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            setSummary(retreiveSettings() + " Seconds");
        } else {
            persistString(defaultValue.toString());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(newValue.toString() + " Seconds");
        return true;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }

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
        numberPicker.setValue(parseInt(retreiveSettings()));
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
