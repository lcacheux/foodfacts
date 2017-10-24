package com.cacheux.foodfacts.barcode;

import android.app.Activity;
import android.content.Intent;

import com.cacheux.foodfacts.R;

public class ZXingMethod implements BarcodeMethod {
    private static final String BS_PACKAGE = "com.google.zxing.client.android";

    @Override
    public int getDisplayName() {
        return R.string.method_zxing;
    }

    @Override
    public boolean isAvailable(Activity activity) {
        return activity.getPackageManager().getLaunchIntentForPackage(BS_PACKAGE) != null;
    }

    @Override
    public void requestBarcode(Activity activity) {
        Intent intent = new Intent(BS_PACKAGE + ".SCAN");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("SCAN_FORMATS", "EAN_13");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        activity.startActivityForResult(intent, BarcodeHandler.CODE_SCAN);
    }

    @Override
    public String parseResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BarcodeHandler.CODE_SCAN && resultCode == Activity.RESULT_OK) {
            return intent.getStringExtra("SCAN_RESULT");
        }
        return null;
    }
}
