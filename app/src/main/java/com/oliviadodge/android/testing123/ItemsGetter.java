package com.oliviadodge.android.testing123;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by oliviadodge on 4/2/2015.
 */
public class ItemsGetter {

    public static final String TAG = "ItemsGetter";
    public static final String ENDPOINT = "http://czshopper.herokuapp.com/items.json";
    public static final String AUTH_TOKEN = "pZpZt8GwRPCauhXyyBD1";
    public static final String METHOD_GET = "GET";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String AUTHORIZATION = "X-CZ-Authorization";

    public ArrayList<GroceryListItem> mGroceryListItems;

    public ArrayList<GroceryListItem> getItems() {

        mGroceryListItems = new ArrayList<>();

        URL url = null;
        try {
            url = new URL(ENDPOINT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            connection.setRequestProperty(ACCEPT_HEADER, "application/json");
            connection.setRequestProperty(CONTENT_TYPE, "application/json");
            connection.setRequestProperty(AUTHORIZATION, AUTH_TOKEN);
            readAndParseJSON(connection);

            getErrorStream(connection);

            Log.i(TAG, "Items successfully downloaded from server");


        } finally {
            connection.disconnect();
        }

        return mGroceryListItems;
    }

    public void readAndParseJSON(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(METHOD_GET);

            InputStream in = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                mGroceryListItems.add(new GroceryListItem(array.getJSONObject(i)));
            }
        }catch (IOException | JSONException e){
            Log.e(TAG, "Problem reading and/or parsing JSON objects", e);
        }
    }

    public void getErrorStream(HttpURLConnection connection){
        byte[]buffer = new byte[1024];
        try {
            Log.i(TAG, "HTTPS response code is " + connection.getResponseCode());
            InputStream eis = connection.getErrorStream();
            if (eis != null) {
                while ((eis.read(buffer, 0, buffer.length)) != -1) {
                    Log.i(TAG, new String(buffer));
                }

                eis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

