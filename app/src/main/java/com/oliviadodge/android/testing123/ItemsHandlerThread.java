package com.oliviadodge.android.testing123;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oliviadodge on 4/4/2015.
 */
public class ItemsHandlerThread<Token> extends HandlerThread {
    private static final String TAG = "ItemsHandlerThread";
    private static final int MESSAGE_DOWNLOAD = 0;
    private static final int MESSAGE_ADD = 1;
    private static final int MESSAGE_EDIT = 2;
    private static final int MESSAGE_DELETE = 3;

    Handler mHandler;
    Handler mResponseHandler;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Listener<Token> mListener;

    public interface Listener<Token> {
        void onItemsDownloaded(ArrayList<String> items);
        void onItemAdded(String item);
        void onItemEdited(String item);
        void onItemDeleted(String item);
    }

    public void setListener(Listener<Token> listener){
        mListener = listener;
    }

    public ItemsHandlerThread(Handler responseHandler){
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueItems(Token token, String string) {
        requestMap.put(token, string);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    public void addItem(Token token, String string) {
        requestMap.put(token, string);
        mHandler.obtainMessage(MESSAGE_ADD, token).sendToTarget();
    }

    public void editItem(Token token, String string) {
        requestMap.put(token, string);
        mHandler.obtainMessage(MESSAGE_EDIT, token).sendToTarget();
    }

    public void deleteItem(Token token, String string) {
        requestMap.put(token, string);
        mHandler.obtainMessage(MESSAGE_DELETE, token).sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if (msg.what == MESSAGE_DOWNLOAD){
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleDownloadRequest(token);
                }
                if (msg.what == MESSAGE_ADD){
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleAddRequest(token);
                }
                if (msg.what == MESSAGE_EDIT){
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleEditRequest(token);
                }
                if (msg.what == MESSAGE_DELETE){
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleDeleteRequest(token);
                }

            }
        };
    }

    private void handleDownloadRequest(final Token token){
        final String string = requestMap.get(token);

        ItemsGetter itemsGetter = new ItemsGetter();
        final ArrayList<String> items = itemsGetter.getItems();

        Log.i(TAG, "Items downloaded: " + items);

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (requestMap.get(token) != string)
                    return;
                requestMap.remove(token);
                mListener.onItemsDownloaded(items);

            }
        });
    }

    private void handleAddRequest(final Token token){
        final String string = requestMap.get(token);
        if (string == null)
            return;

        ItemPoster itemPoster = new ItemPoster(string);
        final String item = itemPoster.postItem();

        Log.i(TAG, "Item added: " + item);

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (requestMap.get(token) != string)
                    return;
                requestMap.remove(token);
                mListener.onItemAdded(item);
            }
        });
    }
    private void handleEditRequest(final Token token){
        final String string = requestMap.get(token);
        if (string == null)
            return;

        ItemUpdater itemUpdater = new ItemUpdater(string);
        final String item = itemUpdater.updateItem();

        Log.i(TAG, "Item edited: " + item);

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (requestMap.get(token) != string)
                    return;
                requestMap.remove(token);
                mListener.onItemEdited(item);
            }
        });
    }
    private void handleDeleteRequest(final Token token){
        final String string = requestMap.get(token);
        if (string == null)
            return;

        ItemDeleter itemDeleter = new ItemDeleter(string);
        itemDeleter.deleteItem();

        Log.i(TAG, "Item deleted");

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (requestMap.get(token) != string)
                    return;
                requestMap.remove(token);
                mListener.onItemDeleted(string);

            }
        });
    }

    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        mHandler.removeMessages(MESSAGE_ADD);
        mHandler.removeMessages(MESSAGE_EDIT);
        mHandler.removeMessages(MESSAGE_DELETE);
        requestMap.clear();
    }

}
