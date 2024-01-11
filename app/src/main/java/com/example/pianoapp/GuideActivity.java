package com.example.pianoapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {
    private TextView guideText;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // You can add TextViews, Images, or other UI elements to display the guide content
         TextView guideText = findViewById(R.id.txtGuide);
        guideText.setText(getString(R.string.piano_playing_guide));
    }


}
