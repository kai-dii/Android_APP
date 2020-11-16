package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.jdbc.BufferRow;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Button btn_1,btn_2,btn_bar,btn_line;
    private TextView view;
    private String result;
    private ArrayList<Integer> number=new ArrayList<>();
    private ArrayList<String> time=new ArrayList<>();
    private ArrayList<String>petfood_weight=new ArrayList<>();
    private ArrayList<String>Leaving_petfood=new ArrayList<>();
    private String JcT="";
    private Intent intent=null;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_1=findViewById(R.id.btn_DATA);
        btn_1.setOnClickListener(mbtnClickListener);
        btn_2=findViewById(R.id.btn_send);
        btn_2.setOnClickListener(mbtnClickListener);
        view=findViewById(R.id.view_1);
        btn_bar=findViewById(R.id.btn_bar);
        btn_bar.setOnClickListener(mbtnClickListener);
        btn_line=findViewById(R.id.btn_line);
        btn_line.setOnClickListener(mbtnClickListener);
        Thread thread = new Thread(mThread);
        thread.start();

    }


    private View.OnClickListener mbtnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int btnid = v.getId();

            if (btnid == R.id.btn_send) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent);
            } else if (btnid == R.id.btn_bar) {
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("number", number);
                bundle.putStringArrayList("creat_at", time);
                bundle.putStringArrayList("petfood_weight", petfood_weight);
                bundle.putStringArrayList("Leavings_petfood", Leaving_petfood);

                intent = new Intent(MainActivity.this, BarChartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (btnid == R.id.btn_line) {
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("number", number);
                bundle.putStringArrayList("creat_at", time);
                bundle.putStringArrayList("petfood_weight", petfood_weight);
                bundle.putStringArrayList("Leavings_petfood", Leaving_petfood);
                intent = new Intent(MainActivity.this, LineChartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    };
    private Runnable mThread=new Runnable() {
        @Override
        public void run() {
            try {

               URL url = new URL("http://192.168.1.28/GetData.php");//http://172.20.10.2/GetData.php
               HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                Log.v("Resp1",Integer.toString(connection.getResponseCode()));
                int responseCode=connection.getResponseCode();

                if(responseCode==HttpURLConnection.HTTP_OK)
                {
                    Log.v("HTTP_OK","OK");
                    InputStream inputStream=connection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    String s="";
                    String line=null;
                    while ((line=bufferedReader.readLine())!=null)
                    {
                        s+=line+"\n";
                    }
                    inputStream.close();
                    result=s;
                 }


                JSONArray array = new JSONArray(result);
                for (int i=0;i<array.length();i++)
                {

                    JSONObject jsonObject=array.getJSONObject(i);
                    String a= jsonObject.getString("number");
                    number.add(jsonObject.getInt("number"));
                    String b=jsonObject.getString("creat_at");
                    time.add(jsonObject.getString("creat_at"));
                    String c= jsonObject.getString("petfood_weight");
                    petfood_weight.add(jsonObject.getString("petfood_weight"));
                    String d=jsonObject.getString("Leavings_petfood");
                    Leaving_petfood.add(jsonObject.getString("Leavings_petfood"));
                    Log.v("print",a+"  "+b+"  "+c+"  "+d);
                    JcT+="Number:"+a+"  Time:"+b+"\n"+"Petfood_Weight:"+c+"  Leavings_petfood:"+d+"\n\n";
                    Log.v("DATATATATA",JcT);
                    Log.v("list_number",number.toString());
                    Log.v("list_time",time.toString());
                    Log.v("list_weight",petfood_weight.toString());
                    Log.v("list_leave",Leaving_petfood.toString());
                }
                final ArrayAdapter timelist=new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,time);
                Log.v("timelist",timelist.toString());




          }
            catch (Exception e)
            {
                result=e.toString();
                Log.v("Wrong",e.toString());
            }

            runOnUiThread(new Runnable() {
                public void run() {

                        view.setText(JcT); // 更改顯示文字

                }
            });
        }
    };


}