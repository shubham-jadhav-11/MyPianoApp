package com.example.pianoapp;
import android.content.Context;
import androidx.annotation.NonNull;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PurchasesUpdatedListener;

public class BillingClient {

    private com.android.billingclient.api.BillingClient billingClient;
    private Context context;

    public BillingClient(Context context) {
        this.context = context;
        initBillingClient();
    }

    private void initBillingClient() {
        com.android.billingclient.api.BillingClient.Builder builder = com.android.billingclient.api.BillingClient.newBuilder(context);
        builder.setListener(purchasesUpdatedListener);
        billingClient = builder.build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                // Handle the case when BillingClient is disconnected.
                // You may want to reconnect in this case.
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                if (billingResponseCode == com.android.billingclient.api.BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready, you can now query purchases and products.
                } else {
                    // Handle the case when BillingClient setup failed.
                    // You may want to log an error or show a message to the user.
                }
            }
        });
    }

    private PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == com.android.billingclient.api.BillingClient.BillingResponseCode.OK && purchases != null) {
            // Handle the list of updated purchases here.
        }
    };

    public void endBillingConnection() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
        }
    }
}
