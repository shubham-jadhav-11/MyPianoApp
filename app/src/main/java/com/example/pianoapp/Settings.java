package com.example.pianoapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String currentTheme = preferences.getString("current_theme", "AppTheme.Light");
        setTheme(currentTheme.equals("AppTheme.Light") ? R.style.AppTheme_Light : R.style.AppTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    // Method to change the app theme



}
