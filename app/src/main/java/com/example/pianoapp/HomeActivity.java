package com.example.pianoapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FILE = 1;
    private static final int RC_SIGN_IN =1;
    private Button btnBrowseMusic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Configure FirebaseUI with the authentication providers you want

        // Get the selected language from SharedPreferences or your preferred storage method
        String selectedLanguage = LocaleHelper.getLanguage(this);

        // Apply the selected language's locale configuration
        Locale newLocale = new Locale(selectedLanguage);
        Locale.setDefault(newLocale);
        FirebaseApp.initializeApp(this);

        Configuration configuration = new Configuration();
        configuration.setLocale(newLocale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        setContentView(R.layout.activity_home);

        View drawerLayout = findViewById(R.id.drawer_layout);
        View menuButton = findViewById(R.id.menuButton);

        // Button for opening the menu
        ImageButton btnOpenMenu = findViewById(R.id.menuButton);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        // Button for starting the MainActivity
        Button btnPlaylists = findViewById(R.id.btnPlaylists);
        btnPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Initialize the Browse Music button
        btnBrowseMusic = findViewById(R.id.btnBrowseMusic);

        // Define an animation for the button
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        btnPlaylists.startAnimation(fadeIn);

        TextView tvWelcome = findViewById(R.id.tvWelcome);

        // Load the animation from the XML resource
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        // Set the animation on the TextView
        tvWelcome.startAnimation(fadein);
        final int REQUEST_PICK_FILE = 1;
        // Set an OnClickListener for the Browse Music button
        btnBrowseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");

                // Start the file picker activity
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });

        Button btnUnlockPremium = findViewById(R.id.btnUnlockPremium);

        btnUnlockPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    // ... (The rest of your code remains the same)




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            // Get the selected file URI
            Uri fileUri = data.getData();

            // Now you can use this fileUri as needed, for example, display its path
            String filePath = fileUri.getPath();
            Toast.makeText(this, "Selected file: " + filePath, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Do something with the signed-in user (e.g., update UI)
            } else {
                // Sign-in failed, check response for details
                if (response != null) {
                    Toast.makeText(this, "Sign-in failed: " + response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click events here
                switch (item.getItemId()) {
                    case R.id.rate:
                        // Handle "Rate App" click
                        showRatingDialog();
                        break;

                    case R.id.share_app:
                        // Handle "Share" click
                        // Example: Share the app (you can replace this with your sharing logic)
                        shareApp();
                        break;
                    case R.id.exit:
                        // Handle "Exit" click
                        // Example: Close the app
                        finish();
                        break;
                    case R.id.help:
                        showHelp();
                        break;
                    case R.id.settings:
                        openSettings();
                        break;
                    case R.id.select_language:
                        showLanguageSelectionDialog();
                        break;

                }
                return true;
            }
        });
        popup.show();
    }

    private void showLanguageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_language);
        final String[] languages = getResources().getStringArray(R.array.languages);

        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedLanguage = languages[which];
                // Change the app's locale and update the UI
                setNewLocale(selectedLanguage);
                showToast("Selected language: " + selectedLanguage);
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click (optional)
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click (optional)
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void setNewLocale(String language) {
        // Create a Configuration object to update the app's locale
        Configuration configuration = getResources().getConfiguration();
        Locale newLocale;

        switch (language) {
            case "English":
                newLocale = new Locale("en"); // Set to English

                break;
            case "हिन्दी":
                newLocale = new Locale("hi"); // Set to Hindi
                break;
            case "मराठी":
                newLocale = new Locale("mr"); // Set to Marathi
                break;
            case "Spanish":
                newLocale = new Locale("es"); // Set to Spanish
                break;
            case "French":
                newLocale = new Locale("fr"); // Set to French
                break;
            default:
                // Use the default locale for unsupported languages
                newLocale = Locale.getDefault();
        }

        // Set the new locale in the app's configuration
        configuration.setLocale(newLocale);

        // Create a Context object with the updated configuration
        Context context = getBaseContext().createConfigurationContext(configuration);

        // Save the new locale for future app launches
        LocaleHelper.setLocale(this, newLocale.getLanguage());

        // Create a new Intent to reload the current activity
        Intent refresh = new Intent(this, HomeActivity.class);
        startActivity(refresh);
        finish(); // Finish the current activity to apply the new locale
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_star_rating, null);
        builder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float rating = ratingBar.getRating();
                // Handle the selected rating (e.g., send it to a server or save it locally)
                showToast("You rated the app: " + rating + " stars");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showHelp() {
        Intent intent = new Intent(this, PianoDescriptionActivity.class);
        startActivity(intent);
    }

    private void shareApp() {
        // Create an intent with ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Set the MIME type to text/plain or any other appropriate type
        shareIntent.setType("text/plain");

        // Set the text to share
        String shareText = "Check out this cool app!"; // Replace with your sharing content
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Start the intent with a chooser
        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void showToast(String message) {
        // Replace this with your preferred way of displaying messages to the user
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // Show a log message to check if this method is called
        super.onBackPressed();
        Log.d("HomeActivity", "Back button pressed");

        // Show the exit confirmation dialog
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false); // Prevent dismissing dialog by tapping outside

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If "Yes" is pressed, finish the activity and exit the app
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static class GoogleBuilder {
        public Object build() {
            return null;
        }
    }

    private static class EmailBuilder {
        public Object build() {
            return null;
        }
    }
}
