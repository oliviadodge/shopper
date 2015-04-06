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

import java.util.Date;

/**
 * Created by oliviadodge on 4/04/2015.
 */
public class EditItemDialogFragment extends DialogFragment {

    public static final String EXTRA_ITEM = "EditItem";
    public GroceryListItem mItem;
    public String mCategory;
    public String mName;

    public static EditItemDialogFragment newInstance(GroceryListItem item){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ITEM, item);

        EditItemDialogFragment fragment = new EditItemDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mItem = (GroceryListItem) getArguments().getSerializable(EXTRA_ITEM);
        mCategory = mItem.getCategory();
        mName = mItem.getName();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_item, null);

        EditText category = (EditText) v.findViewById(R.id.edit_text_category);
        category.setText(mItem.getCategory());
        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setCategory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText name = (EditText) v.findViewById(R.id.edit_text_name);
        name.setText(mItem.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setView(v);
        builder.setTitle(R.string.edit_item_dialog_title);

        builder.setPositiveButton(R.string.new_item_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;

                Intent i = new Intent();
                i.putExtra(EXTRA_ITEM, mItem);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
        });

        builder.setNegativeButton(R.string.new_item_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;

                mItem.setCategory(mCategory);
                mItem.setName(mName);
                Intent i = new Intent();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, i);
            }
        });

        builder.setNeutralButton(R.string.edit_item_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getTargetFragment() == null)
                    return;

                mItem.setCategory(mCategory);
                mItem.setName(mName);
                Intent i = new Intent();
                i.putExtra(EXTRA_ITEM, mItem);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_FIRST_USER, i);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

