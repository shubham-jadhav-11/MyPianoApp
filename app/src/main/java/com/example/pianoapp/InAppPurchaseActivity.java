package com.example.pianoapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;

import java.util.ArrayList;
import java.util.List;

public class InAppPurchaseActivity extends AppCompatActivity {

    private BillingClient billingClient;
    private ListView productListView;
    private Button purchaseButton;
    private TextView titleTextView;
    private List<SkuDetails> skuDetailsList;
    private PurchasesUpdatedListener purchasesUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);

        productListView = findViewById(R.id.productListView);
        purchaseButton = findViewById(R.id.btnPurchase);
        titleTextView = findViewById(R.id.tvTitle);

        // Initialize the purchasesUpdatedListener first
        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @NonNull List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                    // Handle a successful purchase
                    for (Purchase purchase : purchases) {
                        if (isProductValid(purchase)) {
                            // Provide access to premium content
                            grantAccessToPremiumContent(purchase);

                            // Remember to verify purchases and consume them, if necessary.
                            if (!purchase.isAcknowledged()) {
                                // Acknowledge the purchase to prevent it from being re-delivered
                                acknowledgePurchase(purchase);
                            }
                        }
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle user cancellation
                    // You can inform the user that the purchase was canceled
                    showToast("Purchase canceled by the user");
                } else {
                    // Handle purchase errors
                    // Display an error message or take appropriate action
                    showToast("Purchase failed: " + billingResult.getDebugMessage());
                }
            }

            private void acknowledgePurchase(Purchase purchase) {
            }

            private void grantAccessToPremiumContent(Purchase purchase) {

            }

            private boolean isProductValid(Purchase purchase) {
                return false;
            }

        };

        // Then initialize the billing client with the purchasesUpdatedListener
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(purchasesUpdatedListener)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                // Handle billing service disconnection, if needed.
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    loadProductDetails();
                } else {
                    showToast("Billing client setup failed.");
                }
            }
        });

        // Create a list of product items (you should fetch this list from your billing service)
        List<String> productItems = new ArrayList<>();
        productItems.add("Product 1");
        productItems.add("Product 2");
        productItems.add("Product 3");

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, productItems);

        // Set the adapter to the ListView
        productListView.setAdapter(adapter);

        // Set an item click listener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) productListView.getItemAtPosition(position);
                // Handle item click, e.g., initiate the purchase process for the selected product
                showToast("Selected product: " + selectedItem);
            }
        });

        // Set a click listener for the purchase button
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle purchase logic here
                purchaseProduct();
            }
        });

        // Set an OnClickListener for the btnUnlockPremium button
        Button btnUnlockPremium = findViewById(R.id.btnUnlockPremium);

        btnUnlockPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInAppPurchaseActivity();
            }
        });
    }

    private void openInAppPurchaseActivity() {
        // Start the InAppPurchaseActivity when the btnUnlockPremium button is clicked
        Intent intent = new Intent(this, InAppPurchaseActivity.class);
        startActivity(intent);
    }

    private void loadProductDetails() {
        // Load product details from the billing service
        // Use billingClient.querySkuDetailsAsync() to fetch product details
        // Populate skuDetailsList with the retrieved product details
    }

    private void showToast(String message) {
        // Display toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void purchaseProduct() {
        if (isInternetConnectionAvailable()) {
            if (billingClient.isReady()) {
                // Define the product ID(s) you want to purchase
                String productId = "your_product_id"; // Replace with your actual product ID

                // Specify the purchase flow parameters
                SkuDetails skuDetails = null;
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails) // Replace with the SkuDetails for your product
                        .build();

                // Launch the billing flow
                BillingResult billingResult = billingClient.launchBillingFlow(this, billingFlowParams);

                // Check the result of the billing flow
                int responseCode = billingResult.getResponseCode();
                if (responseCode == BillingClient.BillingResponseCode.OK) {
                    // The purchase flow has been initiated successfully.
                    // Wait for the purchase result in the `onPurchasesUpdated` method.
                } else {
                    // Handle the billing flow launch failure, e.g., display an error message.
                    showToast("Failed to initiate purchase flow: " + billingResult.getDebugMessage());
                }
            } else {
                // Billing client is not ready, handle the situation.
                showToast("Billing client is not ready. Please check your internet connection.");
            }
        } else {
            // No internet connection, handle the situation
            showToast("No internet connection. Please check your network settings.");
        }
    }

    private boolean isInternetConnectionAvailable() {
        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


