package com.cacheux.foodfacts.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.utils.Ln;

public class BarcodeDialogFragment extends DialogFragment {
    private EditText editText;
    private Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Ln.v("onAttach " + activity);

        if (activity instanceof Listener) {
            this.listener = (Listener) activity;
        } else {
            Ln.e("BarcodeDialogFragment parent activity must implement Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = getActivity().getLayoutInflater()
                .inflate(R.layout.component_edittext_dialog, null);
        editText = contentView.findViewById(R.id.edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.enter_barcode)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.positiveButtonPressed(editText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.negativeButtonPressed();
                    }
                })
                .setView(contentView);
        return builder.create();
    }

    interface Listener {
        void positiveButtonPressed(String value);
        void negativeButtonPressed();
    }
}
