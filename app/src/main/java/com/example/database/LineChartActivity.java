package com.example.database;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import java.util.Calendar;

public class LineChartActivity extends AppCompatActivity  {

    private LineChart mLineChart;
    private Runnable mRunnable;
    private Handler mHandler;
    private int i = 0;
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> petfood_weight = new ArrayList<>();
    private ArrayList<String> Leaving_petfood = new ArrayList<>();
    private Button btn;
    private  int mYear,mMonth,mDay;
    public String getChooseDate="";
    public String copy="";
    private StringBuffer  stringBuffer=new StringBuffer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mLineChart = findViewById(R.id.chart_line);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(LineChartActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        setDateFormat(year, month, day);
                        String format = "您選擇的日期為:" + getChooseDate;
                        Toast.makeText(LineChartActivity.this,format,Toast.LENGTH_LONG).show();
                        //new_s= getChooseDate.substring(0);
                        //Log.v("new_s",new_s);
                        Log.v("CHOOSE",getChooseDate);
                        mHandler.post(mRunnable);

                    }

                }, mYear,mMonth, mDay).show();

            }

        });

        Bundle bundle = getIntent().getExtras();
        time = bundle.getStringArrayList("creat_at");
        Log.v("time_B", time.toString());
        petfood_weight = bundle.getStringArrayList("petfood_weight");
        Log.v("petfood_weight_B", petfood_weight.toString());
        Leaving_petfood = bundle.getStringArrayList("Leavings_petfood");
        Log.v("Leaving_petfood_B", Leaving_petfood.toString());


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


        for (int i=0;i<petfood_weight.size();i++)
        {
            Log.v("new_time",time.get(i).substring(0,10));
            Log.v("new_date",getChooseDate);
            if((time.get(i).substring(0,10)).equals(getChooseDate))
            {
                float point = Float.parseFloat(petfood_weight.get(i));
                lineMpChart(mLineChart, i, point, 1);
            }
            Log.v("CHOOSE_1",time.get(i).substring(0,10));
            Log.v("equal____",String.valueOf((time.get(i).substring(0,10)).equals(getChooseDate)));
            Log.v("COMPARE",String.valueOf( time.get(i).substring(0,10).compareTo(getChooseDate)));
            Log.v("STRBUFF_",stringBuffer.toString());

        }
        for (int i=0;i<Leaving_petfood.size();i++)
        {
            if(time.get(i).substring(0,10).equals(getChooseDate))
            {
                float point = Float.parseFloat(Leaving_petfood.get(i));
                lineMpChart(mLineChart, i, point, 2);
            }

            Log.v("STRING_DATE_2",time.get(i).substring(0,10));
        }
        Log.v("SIZE",Integer.toString( petfood_weight.size()));
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
    public static void lineMpChart(LineChart lineChart, float time, float value,int many) {

        Log.v("time+",Float.toString(time));
        if (lineChart.getData() == null) {

            LineData lineData = new LineData();
            lineChart.setData(lineData);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);


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
            xAxis.setGranularity(2);
            xAxis.setTextColor(Color.RED);
            xAxis.setGridColor(Color.RED);
            xAxis.setDrawLabels(true);
            xAxis.setEnabled(true);

            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setTextColor(Color.RED);
            yAxis.setGridColor(Color.RED);
            yAxis.setLabelCount(10);
            yAxis.setAxisMinimum(0);
            yAxis.setAxisMaximum(1000);




        } else {
            LineData lineData = lineChart.getLineData();
            ILineDataSet iLineDataSet = lineData.getDataSetByIndex(0);
            ILineDataSet iLineDataSet1=lineData.getDataSetByIndex(1);
            if (iLineDataSet == null || iLineDataSet1==null) {
                iLineDataSet = createLineSet();
                iLineDataSet1=createLineSet2();

                lineData.addDataSet(iLineDataSet);
                lineData.addDataSet(iLineDataSet1);


            }
            if (iLineDataSet.getEntryCount() > 50||iLineDataSet1.getEntryCount()>50) {
                iLineDataSet.removeFirst();
                iLineDataSet1.removeFirst();
            }

            if(many==1)
            {
                iLineDataSet.addEntry(new Entry(time, value, many));
            }
            else if(many==2)
            {
                iLineDataSet1.addEntry(new Entry(time,value,many));
            }

            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.moveViewToX(time);
        }
    }

    private static LineDataSet createLineSet() {
        LineDataSet lineDataSet = new LineDataSet(null, "出料重量");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setDrawValues(true);//顯示數值
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(Color.parseColor("#FF0000"));
        return lineDataSet;
    }
    private static LineDataSet createLineSet2() {
        LineDataSet lineDataSet1 = new LineDataSet(null, "飼料剩下重量");
        lineDataSet1.setDrawValues(true);
        lineDataSet1.setColor(Color.YELLOW);
        lineDataSet1.setHighlightEnabled(true);
        lineDataSet1.setHighLightColor(Color.parseColor("#FF0000"));
        return lineDataSet1;
    }


}
