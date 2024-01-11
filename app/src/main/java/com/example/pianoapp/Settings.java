package com.example.pianoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
    private void changeTheme(String themeName) {
        // Handle different theme options based on the themeName
        switch (themeName) {
            case "AppTheme.Light":
                setTheme(R.style.AppTheme_Light);
                break;
            case "AppTheme.Dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            // Add more cases for other theme options
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        // Save the selected theme to SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("AppSettings", MODE_PRIVATE).edit();
        editor.putString("current_theme", themeName);
        editor.apply();

        // Recreate the activity to apply the new theme
        recreate();
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_theme_to_light:
                changeTheme("AppTheme.Light");
                return true;
            case R.id.action_change_theme_to_dark:
                changeTheme("AppTheme.Dark");
                return true;
            // Add more cases for other menu items as needed
        }
        return super.onOptionsItemSelected(item);
    }
}
