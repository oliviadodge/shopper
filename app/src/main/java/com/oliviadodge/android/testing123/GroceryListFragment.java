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

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 *
 * {@link com.oliviadodge.android.testing123.GroceryListFragment.OnGroceryListItemSelectedListener}
 * interface.
 */
public class GroceryListFragment extends Fragment implements AbsListView.OnItemClickListener{

    public static final int REQUEST_NEW_ITEM = 0;
    public static final int REQUEST_EDIT_ITEM = 1;

    private ArrayList<GroceryListItem> mItems;
    private OnGroceryListItemSelectedListener mListener;
    private static final String TAG = "GroceryListFragment";

    private ItemsHandlerThread<String> mItemsDownloaderThread;

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
    public GroceryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        mItemsDownloaderThread = new ItemsHandlerThread<>(new Handler());
        mItemsDownloaderThread.setListener(new ItemsHandlerThread.Listener<String>() {
            @Override
            public void onItemsDownloaded(ArrayList<GroceryListItem> items) {
                GroceryListItemsLab.get(getActivity()).updateItems(items);
                mItems = GroceryListItemsLab.get(getActivity()).getGroceryListItems();
                setUpAdapter();
            }

            @Override
            public void onItemAdded(GroceryListItem item) {
                Log.i(TAG, "Got item added " + item);
                GroceryListItemsLab.get(getActivity()).add(item);
                updateUI();
            }

            @Override
            public void onItemEdited(GroceryListItem item) {
                Log.i(TAG, "Got newly edited item " + item);
//                GroceryListItemsLab.get(getActivity()).updateByIndex(item);
                updateUI();
            }

            @Override
            public void onItemDeleted(GroceryListItem item) {
                GroceryListItemsLab.get(getActivity()).delete(item);
                updateUI();
            }
        });
        mItemsDownloaderThread.start();
        mItemsDownloaderThread.getLooper();
        Log.i(TAG, "ItemsHandlerThread thread started");


        if (mItems == null)
            mItems = GroceryListItemsLab.get(getActivity()).getGroceryListItems();

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
                if (null != mListener)
                    mListener.onListItemSelected(mItems.get(position).getId());
            }
        });


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            //Use floating context menus on Froyo and GingerBread
            registerForContextMenu(mListView);
        } else {
            //Use contextual action bar on Honeycomb
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    //required, but not used in this implementation
                }

                //ActionMode.Callback methods
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                    //required but not used in this implementation
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete:
//                            GroceryListItemsLab items = GroceryListItemsLab.get(getActivity());
                            for (int i = mAdapter.getCount() -1; i >= 0; i--){
                                if (mListView.isItemChecked(i)){
                                    GroceryListItem gli = mAdapter.getItem(i);
//                                    items.delete(gli);
                                    String token = "delete" + i;
                                    mItemsDownloaderThread.deleteItem(token, gli);
                                }
                            }
                            mode.finish();
                            if (mAdapter != null)
                                mAdapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;

                    }
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    //required but not used in this implementation
                }
            });
        }

        mItemsDownloaderThread.queueItems("download", new GroceryListItem("name", "category"));

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGroceryListItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGroceryListItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onListItemSelected(mAdapter.getItem(position).getId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_item:
                mListener.onNewListItemAdded();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGroceryListItemSelectedListener {
        public void onListItemSelected(int id);
        public void onNewListItemAdded();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_ITEM) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    GroceryListItem gli = (GroceryListItem) data.getSerializableExtra(NewItemDialogFragment.EXTRA_ITEM);
                    Log.i(TAG, "new item added: " + gli);
                    if (mItemsDownloaderThread != null && gli != null){
                         mItemsDownloaderThread.addItem("add", gli);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == REQUEST_EDIT_ITEM) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    GroceryListItem gli = (GroceryListItem) data.getSerializableExtra(EditItemDialogFragment.EXTRA_ITEM);
                    if (mItemsDownloaderThread != null && gli != null){
                        mItemsDownloaderThread.editItem("edit", gli);
                    }
                    break;
                case Activity.RESULT_FIRST_USER:
                    GroceryListItem item = (GroceryListItem) data.getSerializableExtra(EditItemDialogFragment.EXTRA_ITEM);
                    if (mItemsDownloaderThread != null && item != null){
                        mItemsDownloaderThread.deleteItem("delete", item);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mItemsDownloaderThread != null)
            mItemsDownloaderThread.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mItemsDownloaderThread != null)
            mItemsDownloaderThread.quit();
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
