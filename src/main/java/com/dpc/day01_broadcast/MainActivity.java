package com.dpc.day01_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ScreenBroadCastReceiver screenBroadCastReceiver;
    private MediaContentObserver imagesContentObserver = null;
    private MediaContentObserver videoContentObserver = null;
    private static final Uri VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final int MSG_VIDEO_CODE = 1;
    private static final int MSG_IMAGE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         register();
    }

    private void register(){
        screenBroadCastReceiver = new ScreenBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenBroadCastReceiver, filter);

        registerMediaContentLister();
    }

    private  void registerMediaContentLister(){
        imagesContentObserver = new MediaContentObserver(mHandler, this,IMAGE_URI, MSG_IMAGE_CODE);
        getContentResolver().registerContentObserver(IMAGE_URI, true, imagesContentObserver);
        videoContentObserver = new MediaContentObserver(mHandler, this,VIDEO_URI, MSG_VIDEO_CODE);
        getContentResolver().registerContentObserver(VIDEO_URI, true, videoContentObserver);
    }
    private  void unRegisterMediaContentLister(){
        if(imagesContentObserver!=null) {
            getContentResolver().unregisterContentObserver(imagesContentObserver);
            imagesContentObserver = null;
        }
        if(videoContentObserver!=null) {
            getContentResolver().unregisterContentObserver(videoContentObserver);
            videoContentObserver = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadCastReceiver);
        unRegisterMediaContentLister();
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_IMAGE_CODE:
                    Toast.makeText(MainActivity.this,getString(R.string.new_picture)+msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case MSG_VIDEO_CODE:
                    Toast.makeText(MainActivity.this,getString(R.string.new_video)+msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    class ScreenBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive,intent.getAction=" + intent.getAction());
            Toast.makeText(MainActivity.this,intent.getAction(),Toast.LENGTH_SHORT).show();
        }
    }
}
