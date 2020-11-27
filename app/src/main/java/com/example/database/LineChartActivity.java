package com.example.database;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import static android.widget.Toast.LENGTH_LONG;

public class LineChartActivity extends AppCompatActivity  {

    private LineChart mLineChart;
    private Runnable mRunnable;
    private Handler mHandler;
    private static ArrayList<String> datetime = new ArrayList<>();
    private static ArrayList<String> petfood_weight = new ArrayList<>();
    private ArrayList<String> Leaving_petfood = new ArrayList<>();
    private Button btn,btn_update;
    private  int mYear,mMonth,mDay;
    public String getChooseDate="";
    private StringBuffer  stringBuffer=new StringBuffer();
    private static ArrayList<String> datetime_get = new ArrayList<>();
    private String result="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mLineChart = findViewById(R.id.chart_line);
        btn = (Button)findViewById(R.id.btn);
        btn_update=findViewById(R.id.btn_re);
        Bundle bundle = getIntent().getExtras();
        datetime = bundle.getStringArrayList("creat_at");
        Log.v("time_B", Integer.toString( datetime.size()));
        petfood_weight = bundle.getStringArrayList("petfood_weight");
        Log.v("petfood_weight_B", petfood_weight.toString());
        Leaving_petfood = bundle.getStringArrayList("Leavings_petfood");
        Log.v("Leaving_petfood_B", Leaving_petfood.toString());

        btn.setOnClickListener(new View.OnClickListener() { //按鈕監聽  日曆
            @Override
            public void onClick(View v)
            {
                if(v.getId()==R.id.btn)
                {


                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(LineChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            setDateFormat(year, month, day);
                            String format = "您選擇的日期為:" + getChooseDate;
                            Toast.makeText(LineChartActivity.this, format, LENGTH_LONG).show();
                            Log.v("CHOOSE", getChooseDate);
                            mLineChart.clearValues();
                            mHandler.post(mRunnable);

                        }
                    }, mYear, mMonth, mDay).show();

                }

            }

        });

    btn_update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_re)
            {
               Thread thread=new Thread(mThread);
               thread.start();

            }
        }
    });



        lineMpChart(mLineChart, 0, 0, 0);
        initRunnable();
        mHandler.postDelayed(mRunnable, 100);



    }

    private void setDateFormat(int year,int monthOfYear,int dayOfMonth)
    {
        if(dayOfMonth<10 && monthOfYear>=9)
        {
            getChooseDate =  String.valueOf(year) + "-"
                    + String.valueOf(monthOfYear + 1) + "-"
                    + String.valueOf("0"+dayOfMonth);

            int l=getChooseDate.length();
            stringBuffer.delete(0,l);
            stringBuffer.insert(0,getChooseDate);
            Log.v("STRBUFF",stringBuffer.toString());


        }
        if(monthOfYear<9 && dayOfMonth>=10)
        {
            getChooseDate =  String.valueOf(year) + "-"
                    + String.valueOf("0"+(monthOfYear + 1)) + "-"
                    + String.valueOf(dayOfMonth);
            int l=getChooseDate.length();
            stringBuffer.delete(0,l);
            stringBuffer.insert(0,getChooseDate);
            Log.v("STRBUFF",stringBuffer.toString());

        }
        if(dayOfMonth<10 && monthOfYear<9)
        {
            getChooseDate  =  String.valueOf(year) + "-"
                    + String.valueOf("0"+(monthOfYear + 1)) + "-"
                    + String.valueOf("0"+dayOfMonth);
            int l=getChooseDate.length();
            stringBuffer.delete(0,l);
            stringBuffer.insert(0,getChooseDate);
            Log.v("STRBUFF",stringBuffer.toString());
        }
        if (dayOfMonth >= 10 && monthOfYear >= 9) {
            getChooseDate =  String.valueOf(year) + "-"
                    + String.valueOf(monthOfYear + 1) + "-"
                    + String.valueOf(dayOfMonth);

            int l=getChooseDate.length();
            stringBuffer.delete(0,l);
            stringBuffer.insert(0,getChooseDate);
            Log.v("STRBUFF",stringBuffer.toString());
        }
    }


    private void addEnrtyPoint(){

        int n=0;
        for (int i=0;i<petfood_weight.size();i++)
        {
            if((datetime.get(i).substring(0,10)).equals(getChooseDate))
            {
                n++;
                float point = Float.parseFloat(petfood_weight.get(i));
                lineMpChart(mLineChart, i, point, 1); //Float.parseFloat( time.get(i).substring(11,19))
                float points = Float.parseFloat(Leaving_petfood.get(i));
                lineMpChart(mLineChart,i, points, 2);
            }
        }
        if(n==0)
        {
            Toast.makeText(LineChartActivity.this,"今天無任何出料的紀錄",LENGTH_LONG).show();
        }
    }
    private void initRunnable(){
        mHandler=new Handler() ;
        mRunnable=new Runnable() {
            @Override
            public void run() {
                addEnrtyPoint();
            }
        };
    }

    @SuppressWarnings("deprecation")
    public void lineMpChart(LineChart lineChart, int  time, float value, int many) {
        Log.v("time+", Integer.toString(time));


        if (lineChart.getData() == null) {

            LineData lineData = new LineData();
            lineChart.setData(lineData);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);
            lineChart.setDragDecelerationEnabled(true);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDragDecelerationEnabled(true);
            lineChart.setDragDecelerationFrictionCoef(0.9f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.setHighlightPerDragEnabled(true);
            lineChart.setHighlightPerTapEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setMaxHighlightDistance(500f);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(15);
            xAxis.setGranularity(1);
            xAxis.setTextColor(Color.RED);
            xAxis.setGridColor(Color.RED);
            xAxis.setLabelRotationAngle(15);
            xAxis.setDrawLabels(true);
            xAxis.setEnabled(true);
            xAxis.setAvoidFirstLastClipping(true);


            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setTextColor(Color.RED);
            yAxis.setGridColor(Color.RED);
            yAxis.setLabelCount(10);
            yAxis.setAxisMinimum(0);
            yAxis.setAxisMaximum(1000);
        } else {

            XAxis xAxis = lineChart.getXAxis();
            ValueFormatter valueFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    Log.v("get_ch",getChooseDate);
                    if (value >= 0 && datetime.get((int)value).substring(0,10).equals(getChooseDate)&&value <= datetime.size())
                    {
                        return datetime.get((int) value).substring(11,19);
                    }
                    else
                    {
                        return "";
                    }

                }
            };

            xAxis.setValueFormatter(valueFormatter);
            LineData lineData = lineChart.getLineData();
            ILineDataSet iLineDataSet = lineData.getDataSetByIndex(0);
            ILineDataSet iLineDataSet1 = lineData.getDataSetByIndex(1);
            if (iLineDataSet == null || iLineDataSet1 == null) {
                iLineDataSet = createLineSet();
                iLineDataSet1 = createLineSet2();
                lineData.addDataSet(iLineDataSet);
                lineData.addDataSet(iLineDataSet1);

            }
            if (iLineDataSet.getEntryCount() > 50 || iLineDataSet1.getEntryCount() > 50) {
                iLineDataSet.removeFirst();
                iLineDataSet1.removeFirst();
            }
            if (many == 1) {
                iLineDataSet.addEntry(new Entry(time, value, many));
            } else if (many == 2) {
                iLineDataSet1.addEntry(new Entry(time, value, many));
            }

            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.moveViewToX(time);
        }
    }

    private static LineDataSet createLineSet() {
        LineDataSet lineDataSet = new LineDataSet(null, "出料重量(公克g)");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setDrawValues(true);//顯示數值
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(Color.parseColor("#FF0000"));

        return lineDataSet;
    }
    private static LineDataSet createLineSet2() {
        LineDataSet lineDataSet1 = new LineDataSet(null, "飼料剩下重量(公克g)");
        lineDataSet1.setDrawValues(true);
        lineDataSet1.setColor(Color.DKGRAY);
        lineDataSet1.setHighlightEnabled(true);
        lineDataSet1.setHighLightColor(Color.parseColor("#FF0000"));


        return lineDataSet1;
    }

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

                datetime.clear();
                petfood_weight.clear();
                Leaving_petfood.clear();

                JSONArray array = new JSONArray(result);
                for (int i=0;i<array.length();i++)
                {

                    JSONObject jsonObject=array.getJSONObject(i);
                    String b=jsonObject.getString("creat_at");
                    datetime.add(jsonObject.getString("creat_at"));
                    String c= jsonObject.getString("petfood_weight");
                    petfood_weight.add(jsonObject.getString("petfood_weight"));
                    String d=jsonObject.getString("Leavings_petfood");
                    Leaving_petfood.add(jsonObject.getString("Leavings_petfood"));

                }

                mLineChart.clearValues();
                mHandler.post(mRunnable);


            }
            catch (Exception e)
            {
                result=e.toString();
                Log.v("Wrong",e.toString());
            }


        }
    };
}
