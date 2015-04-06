package com.oliviadodge.android.testing123;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GroceryListActivity extends ActionBarActivity implements GroceryListFragment.OnGroceryListItemSelectedListener{
    public static final String TAG = "GroceryListActivity";
    public static final String NEW_ITEM_DIALOG = "NewItemDialogFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new GroceryListFragment())
                    .commit();
        }
    }

    @Override
    public void onListItemSelected(int id) {

    }

    @Override
    public void onNewListItemAdded() {
        FragmentManager fm = getSupportFragmentManager();
        GroceryListFragment glf = (GroceryListFragment) fm.findFragmentById(R.id.fragmentContainer);
        NewItemDialogFragment newItemDialogFragment = new NewItemDialogFragment();
        newItemDialogFragment.setTargetFragment(glf, GroceryListFragment.REQUEST_NEW_ITEM);
        newItemDialogFragment.show(fm, NEW_ITEM_DIALOG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_testing123, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
