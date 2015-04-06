package com.oliviadodge.android.testing123;

import android.net.Uri;
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

/**
 * Created by oliviadodge on 4/3/2015.
 */
public class ItemDeleter {

    public static final String TAG = "ItemDeleter";
    public static final String ENDPOINT = "http://czshopper.herokuapp.com/items/:id.json";
    public static final String AUTH_TOKEN = "pZpZt8GwRPCauhXyyBD1";
    public static final String METHOD_DELETE = "DELETE";
    public static final String AUTHORIZATION = "X-CZ-Authorization";
    public static final String PARAM_ID = "id";

    public GroceryListItem mGroceryListItem;

    public ItemDeleter(GroceryListItem groceryListitem){
        mGroceryListItem = groceryListitem;
    }

    public void deleteItem()  {

        String urlString = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_DELETE)
                .appendQueryParameter(AUTH_TOKEN, AUTH_TOKEN)
                .appendQueryParameter(PARAM_ID, "900")
                .build().toString();

        byte[] buffer = new byte[1024];

        try {

            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestProperty(AUTHORIZATION, AUTH_TOKEN);
            connection.setRequestMethod(METHOD_DELETE);

            connection.setDoOutput(true);
//
//            JSONObject jsonObject = mGroceryListItem.getJsonObject();
//            byte[] byteArray = jsonObject.toString().getBytes();
//            connection.setFixedLengthStreamingMode(byteArray.length);
//
//            OutputStream out = connection.getOutputStream();
//
//            out.write(byteArray);
//
//            out.flush();
//            out.close();
            Log.i(TAG, "HTTPS response code is " + connection.getResponseCode());

            InputStream eis = connection.getErrorStream();
            if (eis != null){
                while ((eis.read(buffer, 0, buffer.length)) != -1) {
                    Log.i(TAG, new String(buffer));
                }

                eis.close();
                connection.disconnect();
            }
//            out.close();
            connection.disconnect();

            Log.i(TAG, "Item deleted from server");

        }catch (IOException e) {
            Log.e(TAG, "problem deleting  ", e);
        }
    }
}
