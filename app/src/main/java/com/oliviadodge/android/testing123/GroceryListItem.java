package com.oliviadodge.android.testing123;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by oliviadodge on 4/1/2015.
 */
public class GroceryListItem implements Serializable{


    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String NAME = "name";

    public int mId;
    public String mCategory;
    public String mName;

    public GroceryListItem(){
        mId = 0;
        mCategory = "";
        mName = "";
    }

    public GroceryListItem(String name, String category) {
        mCategory = category;
        mName = name;
    }

    public GroceryListItem(int id, String name, String category) {
        mId = id;
        mCategory = category;
        mName = name;
    }

    public GroceryListItem(JSONObject jsonObject)throws JSONException {
        mId = jsonObject.getInt(ID);
        mCategory = jsonObject.getString(CATEGORY);
        mName = jsonObject.getString(NAME);
    }


    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public JSONObject getJsonObject()throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, mId);
        jsonObject.put(NAME, mName);
        jsonObject.put(CATEGORY, mCategory);
        return jsonObject;
    }

    public GroceryListItem getObjectFromJson(JSONObject jsonObject)throws JSONException {

        return new GroceryListItem(jsonObject);
    }

    @Override
    public String toString() {
        return (ID + " " + mId + " " + NAME + " " +  mName + " " +  CATEGORY + " " + mCategory);
    }
}
