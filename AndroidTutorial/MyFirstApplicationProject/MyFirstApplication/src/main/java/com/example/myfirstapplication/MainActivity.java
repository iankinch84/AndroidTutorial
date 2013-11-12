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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;

import com.example.myfirstapplication.Database.DataTable;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        this.onCreatedActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Event Handler Ketika Button di-CLICK
     * @param view
     */
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayActivity.class);
        EditText messageEditor = (EditText) this.findViewById(R.id.edit_message);
        String message = messageEditor.getText().toString();

        intent.putExtra(EXTRA_MESSAGE, message);

        Log.d("Send Message", message);

//        //-- Insert into Database
//        DataTable dataComment = new DataTable(this);
//        dataComment.createNewComment(message);
//
//        SharedPreferences pref = this.getSharedPreferences(this.getString(R.string.pref_file_string),
//                                                            Context.MODE_PRIVATE);
//        Log.d("ACTIVITY LOG", pref.getString(this.getString(R.string.pref_file_message_key), "NULL"));

        startActivity(intent);
    }

    /**
     * Ketika Activity Telah selesai dibuat
     */
    public void onCreatedActivity(){
        SharedPreferences pref = this.getSharedPreferences(this.getString(R.string.pref_file_string),
                                                            Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TEMP", "true");
        editor.commit();

        Intent intent = new Intent(this, TestService.class);
        this.startService(intent);
    }

    /*
     *****************************
     * Variable Declaration Area *
     * ***************************
     */
    public static final String EXTRA_MESSAGE = "com.example.myfirstapplication";
}
