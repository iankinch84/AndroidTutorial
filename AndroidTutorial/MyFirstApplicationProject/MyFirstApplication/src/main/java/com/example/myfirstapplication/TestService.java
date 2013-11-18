package com.example.myfirstapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Handler;

/**
 * Created by Ian on 11/12/13.
 */
public class TestService extends IntentService {

    public TestService(){
        super("TestService");
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Log.d("INTENT SERVICE", "CREATING");
        timerHandler = new Handler();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                mainProcess();
            }
        };
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.timerHandler.postDelayed(timerRunnable, 5 * 1000);
    }

    private void mainProcess(){
        SharedPreferences pref =
                this.getSharedPreferences(this.getString(R.string.pref_file_string),
                                            this.MODE_PRIVATE);
        String temp = pref.getString("TEMP", "null");

        if (temp != "null" && temp != "false"){
            Log.d("INTENT SERVICE RUNNING", "Text Test Temp");
        }
        else{
            this.timerHandler.removeCallbacks(timerRunnable);
        }

    }

    /*
     *****************************
     * Variable Declaration Area *
     *****************************
     */
    private Handler timerHandler;
    private Runnable timerRunnable;
}
