package com.example.myfirstapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myfirstapplication.Services.TestService;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

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

        DummyProcForServer();

//        String compressedMessage = this.getCompressedStringByGZip(message);
//        Log.d("GZIP STRING", compressedMessage);
//        Log.d("GZIP STRING", String.valueOf(this.getCRCFromGZippedString(compressedMessage)));
//        Log.d("GZIP STRING", this.getUncompressedGZippedString(compressedMessage));
//
//        try {
//            String keyMsg = "pararato";
//            byte[] keyMsgBytes = keyMsg.getBytes("ISO-8859-1");
//            KeyGenerator keygen = KeyGenerator.getInstance("DES");
//
//            SecretKey key = new SecretKeySpec(keyMsgBytes, "DES/ECB/PKCS5Padding");
//
//            String DESMessage = this.getDESEncryption(message, this.generateDESSecretKey("pa"));
//
////            key = keygen.generateKey();
//            String PlainMessage = this.getDESDecryption(DESMessage, this.generateDESSecretKey("pa"));
//
//            Log.d("DES ENCRYPTION", DESMessage);
//            Log.d("DES ENCRYPTION", PlainMessage);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        intent.putExtra(EXTRA_MESSAGE, message);

        Log.d("Send Message", message);

//        //-- Insert into Database
//        DataTable dataComment = new DataTable(this);
//        dataComment.createNewComment(message);
//
//        SharedPreferences pref = this.getSharedPreferences(this.getString(R.string.pref_file_string),
//                                                            Context.MODE_PRIVATE);
//        Log.d("ACTIVITY LOG", pref.getString(this.getString(R.string.pref_file_message_key), "NULL"));

//        startActivity(intent);
    }

    private void DummyProcForServer(){
        try{
            SecretKey tempKey = this.generateDESSecretKey("arca");
//            String crc = String.format("%i",
//                    (int)this.getCRCFromGZippedString(
//                            this.getCompressedStringByGZip(tempPayload)));
            String compressedString = this.getCompressedStringByGZip(tempPayload);

            byte[] testGzip = this.getCompressedGzip(tempPayload.getBytes("ISO-8859-1"));
            byte[] testDES = this.getDESEncryption(testGzip, tempKey);
            byte[] testBase64 = Base64.encode(testDES, Base64.DEFAULT);

            Log.d("ARCADIA LOG TEST", new String(testGzip, "ISO-8859-1"));
            Log.d("ARCADIA LOG TEST", new String(testDES, "ISO-8859-1"));
            Log.d("ARCADIA LOG TEST", new String(testBase64, "ISO-8859-1"));

            byte[] Base64Test = Base64.decode(testBase64, Base64.DEFAULT);
            byte[] DESTest = this.getDESDecryption(Base64Test, tempKey);
            byte[] GzipTest = this.getUncompressedGzip(DESTest);

            Log.d("ARCADIA LOG TEST", new String(Base64Test, "ISO-8859-1"));
            Log.d("ARCADIA LOG TEST", new String(DESTest, "ISO-8859-1"));
            Log.d("ARCADIA LOG TEST", new String(GzipTest, "ISO-8859-1"));

            long CRC = this.getCRCFromGZippedString(testGzip);
            byte[] CRC_DES = this.getDESEncryption(this.longToBytes(CRC), tempKey);

            MultipartEntity tempEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            ContentBody cb = new InputStreamBody(new ByteArrayInputStream(testGzip), "text-file.txt");


//            long CRCLong = this.getCRCFromGZippedString(compressedString);
//
//            String tempHash = this.getDESEncryption(String.valueOf(CRCLong), tempKey);
//
//            Log.d("ARCADIA LOG", tempHash);
//
//            byte[] tempBody = this.getCompressedByteByGzip(tempPayload);
//
//            MultipartEntity tempEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            ContentBody cb = new InputStreamBody(new ByteArrayInputStream(tempBody), "text-file.txt");
//
//            ByteArrayBody tempArrayBody = new ByteArrayBody(tempBody, "temp");
//
//            String temp3 = Base64.encodeToString(tempHash.getBytes("ISO-8859-1"),
//                    Base64.DEFAULT);
//            String temp4 = new String(Base64.decode(temp3, Base64.DEFAULT), "ISO-8859-1");
//            String temp5 = this.getDESDecryption(temp4, tempKey);
//            String temp6 = this.getUncompressedGZippedString(temp5);
//
//            Log.d("TEST BASE64", temp3);
//            Log.d("TEST BASE64", temp4);
//            Log.d("TEST BASE64", temp5);
//
//
//            StringBody temp2 = new StringBody(Base64.encodeToString(tempHash.getBytes("ISO-8859-1"),
//                                                            Base64.DEFAULT));
//
//            Log.d("ARCADIA LOG", String.valueOf(temp2.getContentLength()));
//            Log.d("ARCADIA LOG", String.valueOf(Base64.encodeToString(tempHash.getBytes("ISO-8859-1"),
//                    Base64.DEFAULT)));
//            Log.d("ARCADIA LOG", String.valueOf(cb.getFilename()));
//
//            tempEntity.addPart("hash", temp2);
//            tempEntity.addPart("gzip", cb);
//
//            HashMap<String, String> tempHashMap = new HashMap<String, String>();
////            tempHashMap.put("hash", tempHash);
//
//            new Server(this,Server.METHOD_POST, tempEntity, "tracker/trackerPost", new TaskCallback() {
//                @Override
//                public void completed(boolean status, String result) {
//                    if (status && result!=null){
//                        Log.d("ArcadiaTracker", "SUBMITTED: " + result);
//                    } else {
//                        Log.d("ArcadiaTracker", "Submit process ERROR: " + result);
//                    }
//                }
//            }, tempHashMap);
        }
        catch (Exception err){
            err.printStackTrace();
        }

    }

    /**
     * Ketika Activity Telah selesai dibuat
     */
    public void onCreatedActivity(){
//        SharedPreferences pref = this.getSharedPreferences(this.getString(R.string.pref_file_string),
//                                                            Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("TEMP", "true");
//        editor.commit();

//        Intent intent = new Intent(this, TestService.class);
//        this.startService(intent);
    }

    /**
     * Compress String with GZip
     *
     * @param _RawString
     * @return
     */
    private String getCompressedStringByGZip(String _RawString){
        String result = "";

        try {
            if (_RawString == null || _RawString.length() == 0) {
                return _RawString;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);

            gzip.write(_RawString.getBytes());
            gzip.close();

            String outStr = out.toString("ISO-8859-1");

            return outStr;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private byte[] getCompressedByteByGzip(String _RawString){
        String result = "";

        try {
            if (_RawString == null || _RawString.length() == 0) {
                return null;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);

            gzip.write(_RawString.getBytes());
            gzip.close();

            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Uncompress String from Gzip
     *
     * @param _GZippedString
     * @return
     */
    private String getUncompressedGZippedString(String _GZippedString){
        String result = "";

        try{
            if (_GZippedString == null || _GZippedString.length() == 0) {
                return _GZippedString;
            }

            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(_GZippedString.getBytes("ISO-8859-1")));
            BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));

            String outStr = "";
            String line;
            while ((line=bf.readLine())!=null) {
                outStr += line;
            }

            return outStr;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private byte[] getUncompressedGzip(byte[] contentBytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    private byte[] getCompressedGzip(byte[] content){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private long getCRCFromGZippedString(byte[] _GzippedString) {
        long result = 0;
        ByteArrayInputStream inputStream;
        CheckedInputStream checkedInputStream;
        byte[] tempBuf = new byte[128];

        try{
            inputStream = new ByteArrayInputStream(_GzippedString);
            checkedInputStream = new CheckedInputStream(inputStream, new CRC32());
            while (checkedInputStream.read(tempBuf) >= 0) {
                //-- Nothing To Do
            }

            result = checkedInputStream.getChecksum().getValue();
        }
        catch (IOException err){
            err.printStackTrace();
        }

        return result;
    }

    private byte[] getDESEncryption(byte[] _PlainText, SecretKey _Key) {
        String temp = "";
        try {
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, _Key);

            byte[] prePocessedText = _PlainText;

            byte[] cipherText = desCipher.doFinal(prePocessedText);


            return cipherText;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] getDESDecryption(byte[] _CipherText, SecretKey _Key){
        try{
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, _Key);

            byte[] prePocessedText = _CipherText;

            byte[] plainText = desCipher.doFinal(prePocessedText);

            return plainText;
        }
        catch (Exception err){
            err.printStackTrace();
        }

        return null;
    }

    private SecretKey generateDESSecretKey(String _Key){
        try{
            while (_Key.length() < 8){
                _Key = "0" + _Key;
            }

            if (_Key.length() > 8){
                _Key = _Key.substring(0, 7);
            }

            byte[] keyMsgBytes = _Key.getBytes("ISO-8859-1");
            return new SecretKeySpec(keyMsgBytes, "DES/ECB/PKCS5Padding");
        }
        catch (Exception err){
            err.printStackTrace();
        }

        return null;
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /*
     *****************************
     * Variable Declaration Area *
     * ***************************
     */
    public static final String EXTRA_MESSAGE = "com.example.myfirstapplication";
    public String tempPayload =
            "{" +
            "  \"data\" : [" +
            "        {" +
            "            \"key\": \"tansmitId\"," +
            "            \"value\": {" +
            "                \"lat\": 122," +
            "                \"lon\": -7," +
            "                \"alt\": 0," +
            "                \"bear\": 0," +
            "                \"time\": 9999," +
            "                \"speed\": 10," +
            "                \"acc\": 1," +
            "                \"pro\": \"GPS\"," +
            "                \"command\": \"LOC\"," +
            "                \"userId\": 196" +
            "            }" +
            "        }" +
            "      ]" +
            "}";
}
