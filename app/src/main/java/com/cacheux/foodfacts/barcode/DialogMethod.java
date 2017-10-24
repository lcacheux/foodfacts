package com.cacheux.foodfacts.barcode;

import android.app.Activity;
import android.content.Intent;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.activity.BarcodeDialogActivity;

public class DialogMethod implements BarcodeMethod {

    public static final String EXTRA_BARCODE = "barcode";

    @Override
    public int getDisplayName() {
        return R.string.method_dialog;
    }

    @Override
    public boolean isAvailable(Activity activity) {
        return true;
    }

    @Override
    public void requestBarcode(Activity activity) {
        Intent intent = new Intent(activity, BarcodeDialogActivity.class);
        activity.startActivityForResult(intent, BarcodeHandler.CODE_SCAN);
    }

    @Override
    public String parseResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BarcodeHandler.CODE_SCAN && resultCode == Activity.RESULT_OK) {
            return intent.getStringExtra(EXTRA_BARCODE);
        }
        return null;
    }
}
