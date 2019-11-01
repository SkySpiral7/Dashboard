package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e449ps.stormy.R;

import androidx.appcompat.app.AppCompatActivity;

public class FakeActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    EditText editText;
    TextView textView;
    Spinner colorSpinner;
    Button launchActivityButton;
    //TODO: figure out test dagger for Espresso and Robolectric
    //TODO: make tests for real classes and delete these

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);

        // Initialize Views
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        launchActivityButton = findViewById(R.id.launchActivityButton);
        linearLayout = findViewById(R.id.linearLayout);
        colorSpinner = findViewById(R.id.colorSpinner);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = tv.getText().toString();
                    textView.setText(text);
                }
                return false;
            }
        });

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                switch (index) {
                    case 0:
                        linearLayout.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        linearLayout.setBackgroundColor(Color.MAGENTA);
                        break;
                    case 2:
                        linearLayout.setBackgroundColor(Color.GREEN);
                        break;
                    case 3:
                        linearLayout.setBackgroundColor(Color.CYAN);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        launchActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FakeActivity.this, OtherActivity.class);
                startActivity(intent);
            }
        });
    }
}
