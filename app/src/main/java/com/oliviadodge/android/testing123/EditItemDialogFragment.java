package com.oliviadodge.android.testing123;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by oliviadodge on 2/20/2015.
 */
public class EditItemDialogFragment extends DialogFragment {

    public static final String EXTRA_ITEM = "EditItem";
    private boolean mSaveNewItem;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_item, null);

        builder.setView(v);
        builder.setTitle(R.string.new_item_dialog_title);

        builder.setPositiveButton(R.string.new_item_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;
                mSaveNewItem = true;

                Intent i = new Intent();
                i.putExtra(EXTRA_ITEM, mSaveNewItem);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
        });

        builder.setNegativeButton(R.string.new_item_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;
                mSaveNewItem = false;

                Intent i = new Intent();
                i.putExtra(EXTRA_ITEM, mSaveNewItem);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, i);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

