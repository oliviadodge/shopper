package com.oliviadodge.android.testing123;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by oliviadodge on 2/20/2015.
 */
public class NewItemDialogFragment extends DialogFragment {

    public static final String EXTRA_ITEM= "NewItem";
    private GroceryListItem mGroceryListItem;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_item, null);

        builder.setView(v);
        builder.setTitle(R.string.new_item_dialog_title);

        mGroceryListItem = new GroceryListItem();
        EditText category = (EditText) v.findViewById(R.id.edit_text_category);
        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGroceryListItem.setCategory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EditText name = (EditText) v.findViewById(R.id.edit_text_name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGroceryListItem.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(R.string.new_item_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;

                Intent i = new Intent();
                i.putExtra(EXTRA_ITEM, mGroceryListItem);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
        });

        builder.setNegativeButton(R.string.new_item_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;

                Intent i = new Intent();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, i);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

