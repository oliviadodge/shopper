package com.oliviadodge.android.testing123;

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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by oliviadodge on 4/1/2015.
 */
public class ItemPoster {

    public static final String TAG = "ItemPoster";
    public static final String ENDPOINT = "http://czshopper.herokuapp.com/items.json";
    public static final String AUTH_TOKEN = "pZpZt8GwRPCauhXyyBD1";
    public static final String METHOD_POST = "POST";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String AUTHORIZATION = "X-CZ-Authorization";

    public GroceryListItem mGroceryListItem;

    public ItemPoster(GroceryListItem groceryListitem){
        mGroceryListItem = groceryListitem;
    }

    public GroceryListItem postItem()  {

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
            sendOutJSON(connection);


            Log.i(TAG, "Item successfully posted to server");
        } finally {
            connection.disconnect();
        }

        return mGroceryListItem;
    }

    public int getItemId(HttpURLConnection connection){
        int id = -1;

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
            id = jsonObject.getInt("id");
            Log.i(TAG, "Got an id from server " + id);

        } catch (IOException | JSONException e) {
            Log.e(TAG, "problem getting or parsing response from server  ", e);
        }

        return id;
    }

    public void sendOutJSON(HttpURLConnection connection){

        try {
            connection.setRequestMethod(METHOD_POST);

            connection.setDoOutput(true);

            JSONObject jsonObject = mGroceryListItem.getJsonObject();
            byte[] byteArray = jsonObject.toString().getBytes();
            connection.setFixedLengthStreamingMode(byteArray.length);

            OutputStream out = connection.getOutputStream();

            out.write(byteArray);

            mGroceryListItem.setId(getItemId(connection));

            out.flush();
            out.close();
        } catch (JSONException | IOException e) {
            Log.e(TAG, "problem adding item ", e);
        }
    }

    public void getErrorStream(HttpURLConnection connection){
        byte[] buffer = new byte[1024];
        try {
            Log.i(TAG, "HTTP response code is " + connection.getResponseCode());
            InputStream eis = connection.getErrorStream();
            if (eis != null){
                while ((eis.read(buffer, 0, buffer.length)) != -1) {
                    Log.i(TAG, new String(buffer));
                }

                eis.close();
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
