package com.example.myfirstapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.example.myfirstapplication.Database.DataTable;

import java.util.Iterator;
import java.util.List;

public class DisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.onCreatedActivity(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_message, container, false);
            return rootView;
        }
    }

    /**
     * Ketika Sebuah Activity Telah dibuat dan siap untuk ditampilkan
     * @param _bundle
     */
    public void onCreatedActivity(Bundle _bundle){
        //-- Get Message from Intent Extra
        Intent intent = this.getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        SharedPreferences pref =
                this.getSharedPreferences(this.getString(R.string.pref_file_string),
                                            Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TEMP", "false");
        editor.commit();

        //-- Create a TextView
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(40);

        //-- Set TextView jadi sebuah View di-Activity sekarang ini
        this.setContentView(textView);

//        //-- GetMessage by ID
//        DataTable dataTable = new DataTable(this);
//        Log.d("ACTIVITY LOG", dataTable.getMessageComment(1));
//
//        //-- Get All Message
//        List<String> allMessage = dataTable.getAllMessage();
//
//        for (Iterator<String> item = allMessage.iterator(); item.hasNext(); ){
//            Log.d("ACTIVITY LOG ALL MESSAGE", item.next());
//        }
    }

    /*
     *****************************
     * Variable Declaration Area *
     *****************************
     */
}
