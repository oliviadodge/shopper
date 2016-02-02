package com.oliviadodge.android.testing123;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 *
 * {@link com.oliviadodge.android.testing123.ReadOnlyGroceryListFragment.OnGroceryListItemSelectedListener}
 * interface.
 */
public class ReadOnlyGroceryListFragment extends Fragment implements AbsListView.OnItemClickListener{

    private ArrayList<GroceryListItem> mItems;
    private static final String TAG = "ReadOnlyGroceryListFragment";

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private GroceryListItemAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReadOnlyGroceryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = GroceryListItemsLab.get(getActivity()).getGroceryListItems();
        setUpAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_read_only_warning), Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        return view;
    }

    void setUpAdapter(){
        if (getActivity() == null || mListView == null) return;

        if (mItems != null){
            mAdapter = new GroceryListItemAdapter(mItems);
            mListView.setAdapter(mAdapter);
        } else
            mListView.setAdapter(null);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_read_only_warning), Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    //Inner class: GroceryListItemAdapter
    private class GroceryListItemAdapter extends ArrayAdapter<GroceryListItem>{

        public GroceryListItemAdapter(ArrayList<GroceryListItem> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //if we aren't given a view, inflate one
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_item, null);
            }

            //configure the view for this crime
            GroceryListItem item = getItem(position);

            TextView categoryTextView =
                    (TextView) convertView.findViewById(R.id.list_item_category);
            categoryTextView.setText(item.getCategory());

            TextView nameTextView =
                    (TextView) convertView.findViewById(R.id.list_item_name);
            nameTextView.setText(item.getName());

            return convertView;
        }
    }
}
