package com.cacheux.foodfacts.barcode;

import android.app.Activity;
import android.content.Intent;

/**
 * Base interface for barcode input methods
 */
public interface BarcodeMethod {
    int getDisplayName();

    /**
     * Return true if the method is available on the current device
     * @param activity
     * @return
     */
    boolean isAvailable(Activity activity);

    /**
     * Launch a new activity used to get the barcode input
     * @param activity
     */
    void requestBarcode(Activity activity);

    /**
     * Parse the result as returned by {@link Activity#onActivityResult(int, int, Intent)}
     * @param requestCode
     * @param resultCode
     * @param intent
     * @return The barcode value or null if not available
     */
    String parseResult(int requestCode, int resultCode, Intent intent);
}
