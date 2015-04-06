package com.oliviadodge.android.testing123;

/**
 * Created by oliviadodge on 4/2/2015.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemUpdater {

    public static final String TAG = "ItemUpdater";
    public static final String ENDPOINT = "http://czshopper.herokuapp.com/items/:id.json";
    public static final String AUTH_TOKEN = "pZpZt8GwRPCauhXyyBD1";
    public static final String METHOD_PUT = "PUT";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String AUTHORIZATION = "X-CZ-Authorization";

    public GroceryListItem mGroceryListItem;

    public ItemUpdater(GroceryListItem groceryListitem){
        mGroceryListItem = groceryListitem;
    }

    public GroceryListItem updateItem()  {

        byte[] buffer = new byte[1024];

        try {

            URL url = new URL(ENDPOINT);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty(ACCEPT_HEADER, "application/json");
            connection.setRequestProperty(CONTENT_TYPE, "application/json");
            connection.setRequestProperty(AUTHORIZATION, AUTH_TOKEN);
            connection.setRequestMethod(METHOD_PUT);

            connection.setDoOutput(true);

            JSONObject jsonObject = mGroceryListItem.getJsonObject();
            byte[] byteArray = jsonObject.toString().getBytes();
            connection.setFixedLengthStreamingMode(byteArray.length);

            OutputStream out = connection.getOutputStream();

            out.write(byteArray);

            mGroceryListItem = getGroceryListItem(connection);

            out.flush();
            out.close();
            Log.i(TAG, "HTTPS response code is " + connection.getResponseCode());

            InputStream eis = connection.getErrorStream();
            if (eis != null){
                while ((eis.read(buffer, 0, buffer.length)) != -1) {
                    Log.i(TAG, new String(buffer));
                }

                eis.close();
                connection.disconnect();
            }
            out.close();
            connection.disconnect();

            Log.i(TAG, "Item successfully posted to server");

        }catch (IOException | JSONException e) {
            Log.e(TAG, "problem posting  ", e);
        }

        return mGroceryListItem;
    }

    public GroceryListItem getGroceryListItem(HttpURLConnection connection){
        GroceryListItem gli = new GroceryListItem();

        try {
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            Log.i(TAG, "Got jsonString from server " + jsonString);

            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();
            gli = gli.getObjectFromJson(jsonObject);

            is.close();

        } catch (IOException | JSONException e) {
            Log.e(TAG, "problem getting or parsing response from server  ", e);
        }

        return gli;
    }

}

