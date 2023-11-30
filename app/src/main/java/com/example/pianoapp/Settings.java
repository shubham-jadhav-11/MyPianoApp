package com.example.pianoapp;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Your code for the Settings activity's onCreate method goes here
    }

    // Define the openSettings method here
    private void openSettings() {
        // Create an intent to open the SettingsActivity
        Intent intent = new Intent(this, Settings.class);

        // Start the settings activity
        startActivity(intent);
    }
}
