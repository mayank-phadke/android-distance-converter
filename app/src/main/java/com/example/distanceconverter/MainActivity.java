package com.example.distanceconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final double MILES_TO_KM = 1.60934;
    private static final double KM_TO_MILES = 0.621371;

    private int current_selection = R.id.milesToKmRadioButton;

    private SharedPref shared_pref;

    private TextView history_text_view, from_text_view, to_text_view, output_text_view;
    private EditText input_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        history_text_view = findViewById(R.id.historyTextView);
        from_text_view = findViewById(R.id.fromText);
        to_text_view = findViewById(R.id.toText);
        output_text_view = findViewById(R.id.outputTextView);
        input_edit_text = findViewById(R.id.inputEditText);

        history_text_view.setMovementMethod(new ScrollingMovementMethod());

        shared_pref = new SharedPref(this);
        if(savedInstanceState == null) {
            int selection = shared_pref.getInt(getString(R.string.selection_key));
            if(selection != 0) {
                current_selection = selection;
            }
        } else {
            String history = savedInstanceState.getString("HISTORY");
            String input = savedInstanceState.getString("INPUT");
            String output = savedInstanceState.getString("OUTPUT");
            int selection = savedInstanceState.getInt("CURRENT_SELECTION");

            history_text_view.setText(history);
            input_edit_text.setText(input);
            output_text_view.setText(output);
            current_selection = selection;
        }

        setLabels(current_selection);
    }

    private void setLabels(int radioId) {
        RadioButton radioButton = findViewById(radioId);
        radioButton.setChecked(true);

        switch (radioId) {
            case R.id.kmToMileRadioButton:
                from_text_view.setText(R.string.kilometers_value);
                to_text_view.setText(R.string.miles_value);
                break;
            case R.id.milesToKmRadioButton:
                from_text_view.setText(R.string.miles_value);
                to_text_view.setText(R.string.kilometers_value);
                break;
        }
    }

    public void changeConversion(View v) {
        int new_selection = v.getId();

        if(new_selection != current_selection) {
            setLabels(new_selection);
        }

        current_selection = new_selection;
    }

    public void convert(View v) {
        double input = 0;
        try {
            input = Double.parseDouble(input_edit_text.getText().toString());
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return;
        }

        String input_string = String.format(Locale.US, "%.1f", input);
        double conversion_factor = 0;
        String new_history_line = input_string + " ";
        String suffix = "";

        switch (current_selection) {
            case R.id.kmToMileRadioButton:
                conversion_factor = KM_TO_MILES;
                new_history_line += "Km ==> ";
                suffix = "Mi";
                break;
            case R.id.milesToKmRadioButton:
                conversion_factor = MILES_TO_KM;
                new_history_line += "Mi ==> ";
                suffix = "Km";
                break;
        }

        double output = input * conversion_factor;

        String output_string = String.format(Locale.US, "%.1f", output);
        output_text_view.setText(output_string);

        new_history_line += output_string + " " + suffix;
        String history = history_text_view.getText().toString();
        history = new_history_line + " \n" + history;
        history_text_view.setText(history);
        input_edit_text.setText("");
    }

    public void clear(View v) {
        history_text_view.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("HISTORY", history_text_view.getText().toString());
        outState.putString("INPUT", input_edit_text.getText().toString());
        outState.putString("OUTPUT", output_text_view.getText().toString());
        outState.putInt("CURRENT_SELECTION", current_selection);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        shared_pref.saveInt(getString(R.string.selection_key), current_selection);

        super.onPause();
    }
}