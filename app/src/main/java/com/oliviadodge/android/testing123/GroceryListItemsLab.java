package com.oliviadodge.android.testing123;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oliviadodge on 2/1/2015.
 */
public class GroceryListItemsLab {

    private ArrayList<GroceryListItem> mGroceryListItems;
    private static GroceryListItemsLab sGroceryListItemsLab;
    private Context mAppContext;
    private static final String TAG = "GroceryListItemsLab";



    public GroceryListItemsLab(Context appContext){
        mAppContext = appContext;
    }

    public static GroceryListItemsLab get(Context context){
        if (sGroceryListItemsLab == null) {
            sGroceryListItemsLab = new GroceryListItemsLab(context.getApplicationContext());
        }
        return sGroceryListItemsLab;
    }

    public void updateItems(ArrayList<GroceryListItem> items){
        mGroceryListItems = items;
    }

    public boolean add(GroceryListItem bc) {
        return mGroceryListItems.add(bc);
    }

    public ArrayList<GroceryListItem> getGroceryListItems(){
        return mGroceryListItems;
    }

    public GroceryListItem getId(int id) {
        for (int i = 0; i < mGroceryListItems.size(); i++) {
            if (mGroceryListItems.get(i).getId() == id) {
                return mGroceryListItems.get(i);
            }
        }
        return null;
    }

    public boolean updateByIndex(GroceryListItem item){
        int indexOfOldItem = -1;
        for(GroceryListItem gli : mGroceryListItems){
            if (gli.getId() == item.getId())
                indexOfOldItem = mGroceryListItems.indexOf(gli);
                break;
        }
        if(indexOfOldItem != -1) {
            mGroceryListItems.set(indexOfOldItem, item);
            return true;
        } else{
            return false;
        }
    }

    public void delete(GroceryListItem item){
        mGroceryListItems.remove(item);
    }
}
