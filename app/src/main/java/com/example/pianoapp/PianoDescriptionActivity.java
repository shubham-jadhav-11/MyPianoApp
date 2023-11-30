package com.example.pianoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PianoDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piano_description);

        // Find the TextViews for the clickable links
        TextView helpCenterLink = findViewById(R.id.helpCenterLink);
        TextView termsOfServiceLink = findViewById(R.id.termsOfServiceLink);
        TextView privacyPolicyLink = findViewById(R.id.privacyPolicyLink);

        // Set an OnClickListener for the Help Center link
        helpCenterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://example.com/help-center");
            }
        });

        // Set an OnClickListener for the Terms of Service link
        termsOfServiceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://example.com/terms-of-service");
            }
        });

        // Set an OnClickListener for the Privacy Policy link
        privacyPolicyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://example.com/privacy-policy");
            }
        });
    }

    // Method to open a web page with the provided URL
    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // This method is already defined in your original code
    public void onHelpCenterClick(View view) {
        openWebPage("https://example.com/help-center");
    }

    // This method is already defined in your original code
    public void onTermsOfServiceClick(View view) {
        openWebPage("https://example.com/terms-of-service");
    }

    // This method is already defined in your original code
    public void onPrivacyPolicyClick(View view) {
        openWebPage("https://example.com/privacy-policy");
    }
}
