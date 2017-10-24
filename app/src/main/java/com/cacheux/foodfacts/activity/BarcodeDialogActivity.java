package com.cacheux.foodfacts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.barcode.DialogMethod;

public class BarcodeDialogActivity extends Activity implements BarcodeDialogFragment.Listener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_dialog);
        BarcodeDialogFragment fragment = new BarcodeDialogFragment();
        fragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void positiveButtonPressed(String value) {
        Intent intent = new Intent();
        intent.putExtra(DialogMethod.EXTRA_BARCODE, value);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void negativeButtonPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
