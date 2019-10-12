package com.example.e449ps.stormy.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.example.e449ps.stormy.R;

public class InternetErrorDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error_title)
                .setMessage(R.string.internet_unavailable_message)
                .setPositiveButton(R.string.ok_text, null)
                .create();
    }
}
