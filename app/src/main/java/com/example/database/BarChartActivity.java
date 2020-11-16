package com.example.database;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class BarChartActivity extends AppCompatActivity {
    private BarChart mBarChart;
    private Runnable mRunnable;
    private Handler mHandler;
    private  int i=0;
    private ArrayList<Integer> number=new ArrayList<>();
    private ArrayList<String> time=new ArrayList<>();
    private ArrayList<String>petfood_weight=new ArrayList<>();
    private ArrayList<String>Leaving_petfood=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        mBarChart=findViewById(R.id.chart_bar);
        Bundle bundle = getIntent().getExtras();
        number=bundle.getIntegerArrayList("number");
        Log.v("number_B",number.toString());
        time=bundle.getStringArrayList("creat_at");
        Log.v("time_B",time.toString());
        petfood_weight=bundle.getStringArrayList("petfood_weight");
        Log.v("petfood_weight_B",petfood_weight.toString());
        Leaving_petfood=bundle.getStringArrayList("Leavings_petfood");
        Log.v("Leaving_petfood_B",Leaving_petfood.toString());
        barMpChart(mBarChart,0,0,0);
        initRunnable();
        mHandler.postDelayed(mRunnable,100);
    }
    private void addEnrtyPoint(){
        for (int i=0;i<petfood_weight.size();i++)
        {
            float point=Float.parseFloat(petfood_weight.get(i));
            barMpChart(mBarChart,i,point,1);
        }
        for (int i=0;i<petfood_weight.size();i++)
        {
            float point=Float.parseFloat(Leaving_petfood.get(i));
            barMpChart(mBarChart,i,point,2);
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
    public static void barMpChart(BarChart barChart, float time, float value,int many) {
        BarData barData = barChart.getData();
        if (barData == null) {
            barData = new BarData();
            barChart.setData(barData);
            barChart.setTouchEnabled(true);
            barChart.setPinchZoom(true);
            barChart.setDragEnabled(true);
            barChart.setDrawValueAboveBar(true);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(12);
            xAxis.setGranularity(2);
            xAxis.setTextColor(Color.RED);
            xAxis.setGridColor(Color.RED);
            xAxis.setEnabled(true);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setTextColor(Color.RED);
            yAxis.setGridColor(Color.RED);
            yAxis.setLabelCount(10);
            yAxis.setAxisMinimum(0);
            yAxis.setAxisMaximum(1500);
        } else {
            IBarDataSet iBarDataSet = barData.getDataSetByIndex(0);
            IBarDataSet iBarDataSet2 = barData.getDataSetByIndex(1);
            if (iBarDataSet == null) {
                iBarDataSet = createBartSet();
                iBarDataSet2 = createBartSet2();
                barData.addDataSet(iBarDataSet);
                barData.addDataSet(iBarDataSet2);
            }
            if(many==1)
            {
                iBarDataSet.addEntry(new BarEntry(time, value,many));

            }
            else if(many==2)
            {
                iBarDataSet2.addEntry(new BarEntry(time, value,many));
            }
            barData.notifyDataChanged();
            barChart.notifyDataSetChanged();
            barChart.moveViewToX(time);
        }
    }

    private static BarDataSet createBartSet() {
        List<BarEntry> barEntryList = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(barEntryList, "出料重量");
        barDataSet.setHighlightEnabled(true);
        barDataSet.setDrawValues(true);
        barDataSet.setHighLightColor(Color.parseColor("#FF0000"));
        return barDataSet;
    }
    private static BarDataSet createBartSet2() {
        List<BarEntry> barEntryList = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(barEntryList, "剩餘重量");
        barDataSet.setColor(Color.YELLOW);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setDrawValues(true);
        barDataSet.setHighLightColor(Color.parseColor("#FF0000"));
        return barDataSet;
    }
}
