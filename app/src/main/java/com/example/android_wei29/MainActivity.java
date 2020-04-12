package com.example.android_wei29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uri=Settings.System.CONTENT_URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    213);
        }else{
            //若已有權限
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        cr=getContentResolver();
        //解的資料在
        // content://Settings 設定資料 URL
        // content://ContactsContract 聯絡人
        // content://Calling 通話紀錄
        // content://MediaStore
    }


    public void test1(View view) {
        // select * from setting
        Cursor c = cr.query(uri,null,null,null,null);
        Log.v("wei","counts:"+c.getColumnCount());

        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex("name"));
            String value=c.getString(c.getColumnIndex("value"));
            Log.v("wei",name+":"+value);
        }
        c.close();
    }

    public void test2(View view) throws Settings.SettingNotFoundException {
        try {
            int b=Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            Log.v("wei","b:"+b);
        }catch(Settings.SettingNotFoundException e){
            Log.v("wei",e.toString());
        }
    }


    public void test3(View view) {
        uri= ContactsContract.Contacts.CONTENT_URI;
        Cursor c = cr.query(uri,null,null,null,null);
        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex("display_name"));
            Log.v("wei","name:"+name);
        }

        c.close();
    }

    //////function/////
    private void printColumnName(Cursor cursor){
        for (int i=0;i<cursor.getColumnCount();i++){
            String filed =cursor.getColumnName(i);
            Log.v("wei",filed);
        }
    }

    public void test4(View view) {
        uri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor c = cr.query(uri,null,null,null,null);
        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex("display_name"));
            String name_2=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone_number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.v("wei",name+":"+phone_number+":"+name_2);
        }
        c.close();
    }


    public void test5(View view) {
        //callog.calls.content_uri
        uri= CallLog.Calls.CONTENT_URI;
        //Calllog.Calls.Number
        //Calllog.Calls.TYPE-->INCOMING, OUTGOING, MISSED
        //Calllog.Calls.DATE
        //Calllog.Calls.DURATION
        Cursor c = cr.query(uri,null,null,null,null);
        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String num=c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String type="";
            if (CallLog.Calls.INCOMING_TYPE==c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type="in";
            }if (CallLog.Calls.OUTGOING_TYPE==c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type="out";
            }if (CallLog.Calls.MISSED_TYPE==c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type="missed";
            }

            Log.v("wei",type+":"+name+":"+num);
        }
        c.close();

    }
}
