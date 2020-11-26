package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_line;
    private TextView view;
    private String result;
    private ArrayList<Integer> number=new ArrayList<>();
    private ArrayList<String> time=new ArrayList<>();
    private ArrayList<String>petfood_weight=new ArrayList<>();
    private ArrayList<String>Leaving_petfood=new ArrayList<>();
    private String JcT="";
    private Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=findViewById(R.id.view_1);
        btn_line=findViewById(R.id.btn_line);
        btn_line.setOnClickListener(mbtnClickListener);
        Thread thread = new Thread(mThread);
        thread.start();
    }

    private View.OnClickListener mbtnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int btnid = v.getId();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("number", number);
            bundle.putStringArrayList("creat_at", time);
            bundle.putStringArrayList("petfood_weight", petfood_weight);
            bundle.putStringArrayList("Leavings_petfood", Leaving_petfood);
           if (btnid == R.id.btn_line) {
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

               URL url = new URL("http://192.168.50.52/GetData.php");
                //URL url = new URL("http://172.20.10.2/GetData.php");//http://172.20.10.2/GetData.php
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

            /*runOnUiThread(new Runnable() {
                public void run() {

                        //view.setText(JcT); // 更改顯示文字

                }
            });*/
        }
    };


}