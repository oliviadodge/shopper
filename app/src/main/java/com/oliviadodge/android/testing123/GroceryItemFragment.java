package com.oliviadodge.android.testing123;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by oliviadodge on 1/31/2015.
 */
public class GroceryItemFragment extends Fragment {

    public static final String TAG = "GroceryItemFragment";
    public static final String EXTRA_ID = "id";


    private GroceryListItem mItem;
    private EditText mNameText;
    private EditText mCategoryText;
    private Callbacks mCallbacks;


    /**
     * Required interface for hosting activities
     */
    public interface Callbacks{
        void onCrossingUpdated(GroceryListItem item);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    public static GroceryItemFragment newInstance(UUID crossingId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ID, crossingId);
        GroceryItemFragment bcf = new GroceryItemFragment();
        bcf.setArguments(args);
        return bcf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getArguments().getInt(EXTRA_ID);
        mItem = GroceryListItemsLab.get(getActivity()).getId(id);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_grocery_item, container, false);


        mCategoryText = (EditText) v.findViewById(R.id.edit_text_category);
        mCategoryText.setText(mItem.getCategory());
        mCategoryText.requestFocus();
        mCategoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setCategory(s.toString());
                mCallbacks.onCrossingUpdated(mItem);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //intentionally left blank
            }
        });

        mNameText = (EditText) v.findViewById(R.id.edit_text_name);
        mNameText.setText(mItem.getName());
        mCategoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setCategory(s.toString());
                mCallbacks.onCrossingUpdated(mItem);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //intentionally left blank
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
//        GroceryListItemsLab.get(getActivity()).saveCrossings();
    }


    @Override
    public void onResume() {
        super.onResume();
        mCategoryText.requestFocus();
    }
}
