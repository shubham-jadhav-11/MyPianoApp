package com.example.pianoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        TextView signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);
        SignInButton signInButton = findViewById(R.id.signInButton);
        Button otpButton = findViewById(R.id.otpButton);

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SendOTPActivity.class));
                // Implement the logic to request OTP here
                // For demonstration purposes, let's show a toast message
                initiateOtpRequest();
                Toast.makeText(LoginActivity.this, "Requesting OTP...", Toast.LENGTH_SHORT).show();
            }

            private void initiateOtpRequest() {
                // Implement the logic to send OTP
                // For demonstration purposes, we'll use Firebase Phone Authentication
                // You should replace this with your actual OTP sending logic

                // Get the user's phone number from the EditText
                String phoneNumber = loginUsername.getText().toString().trim();

                // Use Firebase PhoneAuthProvider to send the OTP
                FirebaseAuth auth = FirebaseAuth.getInstance();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60, // Timeout duration
                        TimeUnit.SECONDS,
                        LoginActivity.this, // Activity (context) for callback
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // This callback will be invoked in two situations:
                                // 1 - Instant verification. In some cases, the phone number can be instantly
                                //     verified without needing to send or enter an OTP.
                                // 2 - Auto-retrieval. On some devices, Google Play services can automatically
                                //     detect the incoming verification SMS and perform verification without
                                //     user action.
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // This callback is invoked for invalid requests, such as an invalid phone number format.
                                Log.e("Firebase Phone Auth", "Verification failed", e);
                                Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number
                                // You can save the verification ID and use it to sign in the user later
                                // For now, we'll just show a toast message
                                Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();

                                // Store the verification ID and resending token to use later
                                // You can save these to shared preferences or a database
                                // for later use when the user enters the OTP
                                // For example:
                                // MySharedPreferences.saveVerificationId(verificationId);
                                // MySharedPreferences.saveResendingToken(token);

                                // Launch the OTP verification screen
                                Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        });
            }

            private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
                // Implement the logic to sign in with the phone auth credential
                // This is called when the SMS code is automatically retrieved or the user enters the code
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = task.getResult().getUser();
                                    Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();

                                    // TODO: Add your code to handle successful authentication
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                                    // TODO: Handle authentication failure
                                }
                            }
                        });
            }
        });

        boolean showOtpButton = true;
        otpButton.setVisibility(showOtpButton ? View.VISIBLE : View.GONE);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // you can also use R.string.default_web_client_id
                .requestEmail()
                .build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        // Add a TextWatcher to the password field to enable/disable the login button based on input
        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do nothing before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do nothing when text is changing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Enable the login button only if the password is not empty
                loginButton.setEnabled(!editable.toString().trim().isEmpty());
            }
        });

        loginButton.setOnClickListener(view -> {
            if (validateUsername() && validatePassword()) {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }


    private void showGoogleAccountPicker() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String displayName = account.getDisplayName();
            String email = account.getEmail();
            Toast.makeText(this, "Google Sign-In successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("displayName", displayName);
            intent.putExtra("email", email);
            startActivity(intent);

            // TODO: Perform your actions with the signed-in account
        } catch (ApiException e) {
            Log.e("GoogleSignInError", "Error: " + e.getStatusCode());
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }

    public boolean validateUsername() {
        String val = loginUsername.getText().toString().trim();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = loginPassword.getText().toString().trim();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        loginUsername.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




    private void checkPassword(DataSnapshot userSnapshot, String userPassword) {
        DataSnapshot userNode = userSnapshot.getChildren().iterator().next();

        String passwordFromDB = userNode.child("password").getValue(String.class);

        if (passwordFromDB.equals(userPassword)) {
            loginUsername.setError(null);

            String nameFromDB = userNode.child("name").getValue(String.class);
            String emailFromDB = userNode.child("email").getValue(String.class);
            String phoneFromDB = userNode.child("phone").getValue(String.class);
            String usernameFromDB = userNode.child("username").getValue(String.class);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            intent.putExtra("name", nameFromDB);
            intent.putExtra("email", emailFromDB);
            intent.putExtra("phone", phoneFromDB);
            intent.putExtra("username", usernameFromDB);
            intent.putExtra("password", passwordFromDB);

            startActivity(intent);
            saveLoginState();
        } else {
            loginPassword.setError("Invalid Credentials");
            loginPassword.requestFocus();
        }
    }

    private void saveLoginState() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        // Add any additional user information that you want to persist
        // editor.putString("userId", userId);
        editor.apply();
    }

    // Add this method to show a dialog for the "forgot password" feature
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your email to reset your password");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();

                // Perform the logic to send a password reset email
                sendPasswordResetEmail(email);
            }

            private void logout() {
                // Clear user login state in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", false);
                // Clear any additional user information if needed
                // editor.remove("userId");
                editor.apply();

                // Implement any additional logout logic (e.g., Firebase logout)
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen or another appropriate destination
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                finish(); // Finish the current activity
            }

            private void sendPasswordResetEmail(String email) {
                // Add this method to send a password reset email (you should implement this based on your authentication provider)
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                    Log.e("PasswordReset", "Error: " + task.getException().getMessage());
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}

