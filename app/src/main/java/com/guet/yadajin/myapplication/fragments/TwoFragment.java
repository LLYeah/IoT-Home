package com.guet.yadajin.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.guet.yadajin.myapplication.R;

import java.text.DecimalFormat;

public class TwoFragment extends Fragment {

    private final int HIGH = 0;
    private final int LOW = 1;
    private LineChart mChart1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("%%%%%%%%%%%%%%%%%"+OneFragment.flag);
        if (OneFragment.flag){
            initView(view);
        }
    }
    private void initView(View view){
        mChart1 = (LineChart) view.findViewById(R.id.chart1);
        showChart(getLineData());
        networkThread.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dynamic_line_chart, container, false);
        return rootView;
    }

    private Thread networkThread = new Thread(){
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    addChartEntry();
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    // 为高温线和低温线添加一个坐标点
    private void addChartEntry() {
        // 获取图表数据
        LineData lineData1 = mChart1.getData();

        // 添加横坐标值
        lineData1.addXValue((lineData1.getXValCount()) + "");

        // 增加温度
        LineDataSet highLineDataSet1 = lineData1.getDataSetByIndex(HIGH);
        float temp = Float.parseFloat(OneFragment.strArr[0]);//温度值
        System.out.println("&&&"+temp);
        Entry entryTemp = new Entry(temp, highLineDataSet1.getEntryCount());
        lineData1.addEntry(entryTemp, HIGH);

        // 增加湿度
        LineDataSet lowLineDataSet1 = lineData1.getDataSetByIndex(LOW);
        float humi = Float.parseFloat(OneFragment.strArr[1]);//湿度值
        Entry entryLow = new Entry(humi, lowLineDataSet1.getEntryCount());
        lineData1.addEntry(entryLow, LOW);

        // 使用新数据刷新图表
        mChart1.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart1.setVisibleXRangeMaximum(12);
        mChart1.moveViewToX(lineData1.getXValCount() - 12);
    }
    /**
     * 获取折线数据
     *
     * @return
     */
    private LineData getLineData() {
        // 创建折线数据
        LineData lineData = new LineData();
        // 添加数据集
        lineData.addDataSet(getHighLineDataSet());
        lineData.addDataSet(getLowLineDataSet());
        // 返回折线数据
        return lineData;
    }
    /**
     * 显示图表
     */
    private void showChart(LineData lineData) {
        // 初始化图表
        initChart1();
        // 数据显示的颜色
        lineData.setValueTextColor(Color.BLACK);
        // 给图表设置数据
        mChart1.setData(lineData);
    }
    /**
     * 初始化图表1
     */
    private void initChart1() {
        // 设置描述
        mChart1.setDescription("动态折线图");
        // 设置可触摸
        mChart1.setTouchEnabled(true);
        // 可拖曳
        mChart1.setDragEnabled(true);
        // 可缩放
        mChart1.setScaleEnabled(true);
        // 设置绘制网格背景
        mChart1.setDrawGridBackground(true);
        mChart1.setPinchZoom(true);
        // 设置图表的背景颜色
        mChart1.setBackgroundColor(0xfff5f5f5);
        // 图表注解（只有当数据集存在时候才生效）
        Legend legend = mChart1.getLegend();
        // 设置图表注解部分的位置
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        // 线性，也可是圆
        legend.setForm(Legend.LegendForm.LINE);
        // 颜色
        legend.setTextColor(Color.BLUE);
        // x坐标轴
        XAxis xl = mChart1.getXAxis();
        xl.setTextColor(0xff00897b);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        // 几个x坐标轴之间才绘制
        xl.setSpaceBetweenLabels(5);
        // 如果false，那么x坐标轴将不可见
        xl.setEnabled(true);
        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 图表左边的y坐标轴线
        YAxis leftAxis = mChart1.getAxisLeft();
        leftAxis.setTextColor(0xff37474f);
        // 最大值
        leftAxis.setAxisMaxValue(100f);
        // 最小值
        leftAxis.setAxisMinValue(-10f);
        // 不一定要从0开始
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart1.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis.setEnabled(false);
    }
    // 初始化数据集，添加一条高温统计折线
    private LineDataSet getHighLineDataSet() {
        LineDataSet set = new LineDataSet(null, "温度");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 折线的颜色
        set.setColor(Color.RED);
        set.setCircleColor(Color.YELLOW);
        set.setLineWidth(2f);
        set.setCircleSize(8f);
        set.setFillAlpha(128);
        set.setCircleColorHole(Color.BLUE);
        set.setHighLightColor(Color.GREEN);
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(10f);
        set.setDrawValues(true);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat(".0℃");
                String s = decimalFormat.format(value);
                return s;
            }
        });
        return set;
    }
    // 初始化数据集，添加一条低温统计折线
    private LineDataSet getLowLineDataSet() {
        LineDataSet set = new LineDataSet(null, "湿度");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 折线的颜色
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
        set.setCircleSize(8f);
        set.setFillAlpha(128);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.DKGRAY);
        set.setValueTextColor(Color.BLACK);
        set.setCircleColorHole(Color.RED);
        set.setValueTextSize(10f);
        set.setDrawValues(true);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat(".0RH");
                String s = decimalFormat.format(value);
                return s;
            }
        });
        return set;
    }
}
