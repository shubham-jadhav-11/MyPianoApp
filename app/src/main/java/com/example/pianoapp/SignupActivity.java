package com.example.pianoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword, confirmPassword, contact;
    TextView loginRedirectText;
    Button signupButton;
    Spinner countryCodeSpinner;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.contact);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        countryCodeSpinner = findViewById(R.id.countryCodeSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        countryCodeSpinner.setAdapter(adapter);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Get user-entered data
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String confirmedPassword = confirmPassword.getText().toString();
                String contactNumber = contact.getText().toString();

                // Check if any of the fields are empty
                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ||
                        confirmedPassword.isEmpty() || contactNumber.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmedPassword)) {
                    Toast.makeText(SignupActivity.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Generate a unique key for the user
                    String userId = reference.push().getKey();
                    HelperClass helperClass = new HelperClass(name, email, username, password);

                    // Save the user data to the generated key
                    assert userId != null;
                    reference.child(userId).setValue(helperClass);

                    // All fields are filled, proceed with registration
                    // Implement your Firebase Realtime Database logic here
                    // You can use the collected data to create a new user in your database

                    Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected country code
                String selectedCountryCode = parentView.getItemAtPosition(position).toString();

                // Handle the selection, e.g., store it in a variable or use it to format the contact number
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (if needed)
            }
        });
    }
}
