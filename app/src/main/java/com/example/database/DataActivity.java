package com.example.database;

import android.net.wifi.WifiManager;
import android.net.wifi.aware.DiscoverySession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DataActivity extends AppCompatActivity  {
    private static Handler handler;
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private Button btn;
    private Float txt1;
    private Float txt2;
    private String txt3;

    private String  result=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ed1=(EditText) findViewById(R.id.edit_txt1);
        ed2=(EditText) findViewById(R.id.edit_txt2);
        ed3=(EditText) findViewById(R.id.edit_txt3);
        btn=findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Thread ","Thread Start");
                Thread thread=new Thread(mthread);
                thread.start();
            }
        });

    }
   private Runnable mthread=new Runnable() {
        @Override
        public void run() {


            Log.v("run","into run");
            txt1 = Float.valueOf(ed1.getText().toString());
            txt2=Float.valueOf(ed2.getText().toString());
            txt3=ed3.getText().toString();


            Log.v("DATATXT",txt1.toString()+","+txt2.toString());
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.27/SendData.php");//http://172.20.10.2/GetData.php

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("creat_at",txt3));
            params.add(new BasicNameValuePair("petfood_weight", txt1.toString()));
            params.add(new BasicNameValuePair("Leavings_petfood", txt2.toString()));




            try {
                Log.v("try","in try");


                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


                Log.v("HTTP_DATA",params.toString());
                Log.v("POST_RES",httpResponse.toString());
                Log.v("RES2",Integer.toString( httpResponse.getStatusLine().getStatusCode()));

                if(httpResponse.getStatusLine().getStatusCode() == 200){
                    //將Post回傳的值轉為String，將轉回來的值轉為UTF8，否則若是中文會亂碼
                    Log.v("POST_RES","in");
                    HttpEntity entity = httpResponse.getEntity();
                    Log.v("RES_DATA",entity.toString());

                   String strResult = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8).toString(); // 1


                    if (strResult == null || "".equals(strResult)) {
                        // no entity or empty entity
                        Log.v("POST_DATA","empty");
                        Log.v("RES_DATA",strResult);
                    } else {
                        // got something
                        Log.v("POST_DATA","have Entity");
                        Log.v("RES_DATA",strResult);

                    }
                    //Log.v("string ",strResult);
                    Log.v("end","END");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Looper.prepare();
            Toast.makeText(DataActivity.this,"Data Sumbit sucessfully",Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    };


}
