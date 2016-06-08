package com.dpc.day01_broadcast;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by dupengcan on 16-5-23.
 */
public class MediaContentObserver extends ContentObserver {
    private Uri uri;
    private Handler mHandler;
    private Context mContext;
    private ContentResolver mContentResolver;
    private int mMsgCode;
    private String TAG = "MediaContentObserver";
    private static final String[] PROJECTION = new String[]{
            BaseColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_MODIFIED,
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_DISPLAY_NAME = 1;
    public static final int INDEX_MIME_TYPE = 2;
    public static final int INDEX_DATA = 3;
    public static final int INDEX_SIZE = 4;
    public static final int INDEX_THUMBNAILDATA = 5;

    private static final String ORDER_COLUMN =
            MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC, " +
                    BaseColumns._ID + " DESC ";

    public MediaContentObserver(Handler handler, Context context, Uri uri, int msgCode){
        super(handler);
        this.mHandler = handler;
        this.mContext = context;
        this.uri = uri;
        this.mMsgCode = msgCode;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d(TAG, "onChange=" + selfChange);
        Cursor cursor = mContentResolver.query(uri,
                PROJECTION,
                null,
                null,
                ORDER_COLUMN);

        if(cursor == null || !cursor.moveToFirst()){
        }else{
            do{
                String setDisplayName =cursor.getString(INDEX_DISPLAY_NAME);
                Log.v(TAG, "setDisplayName : " + setDisplayName);
                Message message = Message.obtain();
                message.what = mMsgCode;
                message.obj = setDisplayName;
                mHandler.sendMessage(message);
                break;
            }while(cursor.moveToNext());
            cursor.close();
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }

}
