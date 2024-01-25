package com.example.pianoapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the view that you want to animate
        View splashView = findViewById(R.id.tvSplashText);
        ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Set up the fadeIn animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
        // Set the animation to the view
        splashView.startAnimation(fadeIn);

        // Set a listener to handle the animation end event (optional)
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start event
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation end event
                // Proceed to the next screen or perform other actions
                checkDisclaimerAndNavigate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkDisclaimerAndNavigate() {
        // Check if the disclaimer has been accepted, if not, show the disclaimer dialog
        if (!isDisclaimerAccepted()) {
            showDisclaimerDialog();
        } else {
            // Use a Handler to delay the transition to LoginActivity
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Optionally, finish the SplashActivity to prevent going back to it
                }
            }, SPLASH_DELAY);
        }
    }

    private void showDisclaimerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disclaimer");
        builder.setMessage(getString(R.string.disclaimer_text));

        // Set up the Accept button click listener
        builder.setPositiveButton("I Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the acceptance state
                saveDisclaimerAcceptance();

                // Proceed to the next screen
                navigateToNextScreen();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isDisclaimerAccepted() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("disclaimer_accepted", false);
    }

    private void saveDisclaimerAcceptance() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disclaimer_accepted", true);
        editor.apply();
    }

    private void navigateToNextScreen() {
        // Use a Handler to delay the transition to LoginActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optionally, finish the SplashActivity to prevent going back to it
            }
        }, SPLASH_DELAY);
    }
}
