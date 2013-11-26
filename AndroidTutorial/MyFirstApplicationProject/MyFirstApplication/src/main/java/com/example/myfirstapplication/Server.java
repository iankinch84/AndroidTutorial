package com.example.myfirstapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 7/18/13.
 */
public class Server extends AsyncTask<Void, Integer, Boolean> {
    public static final boolean METHOD_GET = true;
    public static final boolean METHOD_POST = false;
    public static final String PREFS_KEY = "secure.tracker.sharedpreferences";
    public static final String PREFS_KEY_STOP = "secure.tracker.stopsharedpreferences";
    public static final String PREFS_KEY_MAIN = "secure.tracker.mainsharedpreferences";
    public static final String PREFS_KEY_USER_JSESSIONID = "JSESSIONID";
    public static final String PREFS_KEY_USER_AWSELB = "AWSELB";
    public static final String PREFS_KEY_USER_LOGIN_TOKEN = "LOGINTOKEN";

    private TaskCallback mCallback;
    private DefaultHttpClient httpclient;
    private HttpPost httppost;
    private HttpGet httpget;
    private HttpResponse httpresponse;
    private List<NameValuePair> param;
    private MultipartEntity mparam;
    private boolean method;
    private String url;
    private Context context;
    private String returnValue;

    public DefaultHttpClient getHttpclient() {
        return httpclient;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Server(Context context, boolean method, List<NameValuePair> data, String url, TaskCallback callback){
        this.context = context;
        this.method = method;
        this.param = data;
        this.mparam = null;
        this.url = url;
        this.mCallback = callback;
        httpclient = new DefaultHttpClient();
        if (method){
            String paramString = "";
            if (param != null) paramString = URLEncodedUtils.format(param, "utf-8");
            httpget = new HttpGet(context.getString(R.string.server_url) + url + "?" + paramString);
        } else {
            httppost = new HttpPost(context.getString(R.string.server_url) + url);
            if (param != null) try {
                httppost.setEntity(new UrlEncodedFormEntity(param));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(Server.PREFS_KEY_MAIN, Context.MODE_PRIVATE);
        String cookie = prefs.getString(PREFS_KEY_USER_JSESSIONID,null);
        String cookie2 = prefs.getString(PREFS_KEY_USER_AWSELB,null);
        if (cookie != null && cookie2 != null ){
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
        } else if (cookie != null) {
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            this.execute((Void)null);
        }
    }

    /**
     * Konek ke server dengan tambahan Header Cookie
     * @param context
     * @param method
     * @param data
     * @param url
     * @param callback
     * @param _Header
     */
    public Server(Context context, boolean method, List<NameValuePair> data, String url, TaskCallback callback, HashMap<String, String> _Header){
        this.context = context;
        this.method = method;
        this.param = data;
        this.mparam = null;
        this.url = url;
        this.mCallback = callback;
        httpclient = new DefaultHttpClient();
        if (method){
            String paramString = "";
            if (param != null) paramString = URLEncodedUtils.format(param, "utf-8");
            httpget = new HttpGet(context.getString(R.string.server_url) + url + "?" + paramString);
        } else {
            httppost = new HttpPost(context.getString(R.string.server_url) + url);
            if (param != null) try {
                httppost.setEntity(new UrlEncodedFormEntity(param));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(Server.PREFS_KEY_MAIN, Context.MODE_PRIVATE);
        String cookie = prefs.getString(PREFS_KEY_USER_JSESSIONID,null);
        String cookie2 = prefs.getString(PREFS_KEY_USER_AWSELB,null);
        if (cookie != null && cookie2 != null ){
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
        } else if (cookie != null) {
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
        }

        //-- Tambahan
        for (String temp : _Header.keySet()){
            if (method)
                httpget.setHeader("Cookie",temp + "="+ _Header.get(temp));
            else
                httppost.setHeader("Cookie",temp + "="+ _Header.get(temp));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            this.execute((Void)null);
        }
    }

    public Server(Context context, boolean method, MultipartEntity data, String url, TaskCallback callback, HashMap<String, String> _Header){
        this.context = context;
        this.method = method;
        this.param = null;
        this.mparam = data;
        this.url = url;
        this.mCallback = callback;
        httpclient = new DefaultHttpClient();
        if (method){
            String paramString = "";
            if (param != null) paramString = URLEncodedUtils.format(param, "utf-8");
            httpget = new HttpGet(context.getString(R.string.server_url) + url + "?" + paramString);
        } else {
            httppost = new HttpPost(context.getString(R.string.server_url) + url);
            if (param != null) try {
                httppost.setEntity(new UrlEncodedFormEntity(param));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (mparam!=null) httppost.setEntity(mparam);
        }

        SharedPreferences prefs = context.getSharedPreferences(Server.PREFS_KEY_MAIN, Context.MODE_PRIVATE);
        String cookie = prefs.getString(PREFS_KEY_USER_JSESSIONID,null);
        String cookie2 = prefs.getString(PREFS_KEY_USER_AWSELB,null);
        if (cookie != null && cookie2 != null ){
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie+";"+PREFS_KEY_USER_AWSELB+"="+cookie2);
        } else if (cookie != null) {
            if (method)
                httpget.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
            else
                httppost.setHeader("Cookie",PREFS_KEY_USER_JSESSIONID + "="+cookie);
        }

        //-- Tambahan
        for (String temp : _Header.keySet()){
            if (method)
                httpget.setHeader("Cookie",temp + "="+ _Header.get(temp));
            else
                httppost.setHeader("Cookie",temp + "="+ _Header.get(temp));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            this.execute((Void)null);
        }
    }

    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public static final String ERROR_NO_CONNECTION = "No connection";
    @Override
    protected Boolean doInBackground(Void... params) {
        if (hasConnection()){
            try {
                if (method){
                    httpresponse = httpclient.execute(httpget);
                } else {
                    httpresponse = httpclient.execute(httppost);
                }
                StatusLine statusLine = httpresponse.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                    returnValue = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");
                    return true;
                } else {
                    returnValue = String.valueOf(statusLine.getStatusCode());
                    return false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnValue = null;
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                returnValue = null;
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                returnValue = null;
                return false;
            }
        } else {
            returnValue = ERROR_NO_CONNECTION;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (this.mCallback != null && !isCancelled())
            this.mCallback.completed(success, returnValue);
    }
}
