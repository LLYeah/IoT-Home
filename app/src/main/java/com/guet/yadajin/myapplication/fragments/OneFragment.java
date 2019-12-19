package com.guet.yadajin.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.guet.yadajin.myapplication.R;
import com.guet.yadajin.myapplication.TcpclientActivity;

import java.net.InetAddress;
import java.net.Socket;

public class OneFragment extends Fragment {

    EditText iptoedit;//ip编辑框对象
    EditText porttoedit;//端口编辑框对象
    TextView temp;//温度
    TextView humi;//湿度
    Switch aSwitch ;
    android.widget.Button Button;//连接服务器按钮对象
    java.net.Socket Socket = null;//Socket
    boolean buttontitle = true;//定义一个逻辑变量，用于判断连接服务器按钮状态
    boolean RD = false;//用于控制读数据线程是否执行
    LineChart mChart1;

    java.io.OutputStream OutputStream = null;//定义数据输出流，用于发送数据
    java.io.InputStream InputStream = null;//定义数据输入流，用于接收数据

    private Handler mainHandle = new Handler(Looper.getMainLooper());
    static String[] strArr = new String[3];
    String ID="0001";//安卓端设备序号
    String TO="0002";//硬件端设备序号
    String[] msg_to_send={"LED_ON","LED_OFF","ISK"};
    String led_on  = "{\"id\":\""+ID+"\",\"to\":\""+TO+"\",\"msg\":\""+msg_to_send[0]+"\"}";
    String led_off = "{\"id\":\""+ID+"\",\"to\":\""+TO+"\",\"msg\":\""+msg_to_send[1]+"\"}";
    String led_isk = "{\"id\":\""+ID+"\",\"to\":\""+TO+"\",\"msg\":\""+msg_to_send[2]+"\"}";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        iptoedit = (EditText)view.findViewById(R.id.IPEdit);//获取ip地址编辑框对象
        porttoedit = (EditText)view.findViewById(R.id.portEdit);//获取端口编辑框对象
        Button = (android.widget.Button)view.findViewById(R.id.Button);//获取连接服务器按钮对象
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link(view);
            }
        });
        android.widget.Button btn1 = view.findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });

        mChart1 = (LineChart) view.findViewById(R.id.chart1);

        temp = (TextView) view.findViewById(R.id.temperature);//获得温度值
        humi = (TextView) view.findViewById(R.id.humidity);//获得湿度值
        aSwitch= (Switch) view.findViewById(R.id.ON_OFF);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ThreadSendData t1 = new ThreadSendData(led_on);
                    t1.start();
                } else{
                    ThreadSendData t1 = new ThreadSendData(led_off);
                    t1.start();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tcpclient, container, false);
        return rootView;
    }

    public void buttonClick(){
        // 响应事件
        Toast ts = Toast.makeText(getContext(),"发送测试成功！",Toast.LENGTH_LONG);
        ts.show();
        ThreadSendData t1 = new ThreadSendData(led_isk);
        t1.start();
    }
    //连接服务器按钮按下
    public void link(View view){
        //判断按钮状态
        if (buttontitle == true){
            //如果按钮没有被按下，则按钮状态改为按下
            buttontitle = false;
            //读数据线程可以执行
            RD = true;
            //并创建一个新的线程，用于初始化socket
            Connect_Thread Connect_thread = new Connect_Thread();
            Connect_thread.start();
            //改变按钮标题
            Button.setText("断 开 连 接");
        }else{
            //如果按钮已经被按下，则改变按钮标题
            Button.setText("连 接 服 务 器");
            //储存状态的变量反转
            buttontitle = true;
            try{
                //取消socket
                Socket.close();
                //socket设置为空
                Socket = null;
                //读数据线程不执行
                RD = false;
            }catch (Exception e){
                //如果想写的严谨一点，可以自行改动
                e.printStackTrace();
            }
        }
    }
    //用线程创建Socket连接
    class Connect_Thread extends Thread {
        public void run(){
            //定义一个变量用于储存ip
            InetAddress ipAddress;
            try {
                //判断socket的状态，防止重复执行
                if (Socket == null) {
                    //如果socket为空则执行
                    //获取输入的IP地址
                    ipAddress = InetAddress.getByName(iptoedit.getText().toString());
                    //获取输入的端口
                    int port = Integer.valueOf(porttoedit.getText().toString());
                    //新建一个socket
                    Socket = new Socket(ipAddress, port);
                    //获取socket的输入流和输出流
                    InputStream = Socket.getInputStream();
                    OutputStream = Socket.getOutputStream();

                    //新建一个线程读数据
                    ThreadReadData t1 = new ThreadReadData();
                    t1.start();
                }
            } catch (Exception e) {
                //如果有错误则在这里返回
                e.printStackTrace();
            }
        }
    }
    //用线程执行读取服务器发来的数据
    class ThreadReadData extends Thread {
        public void run() {
            //定义一个变量用于储存服务器发来的数据
            String textdata;
            //根据RD变量的值判断是否执行读数据
            while (RD) {
                try {
                    //定义一个字节集，存放输入的数据，缓存区大小为2048字节
                    final byte[] ReadBuffer = new byte[2048];
                    //用于存放数据量
                    final int ReadBufferLengh;
                    //从输入流获取服务器发来的数据和数据宽度
                    //ReadBuffer为参考变量，在这里会改变为数据
                    //输入流的返回值是服务器发来的数据宽度
                    ReadBufferLengh = InputStream.read(ReadBuffer);

                    //验证数据宽度，如果为-1则已经断开了连接
                    if (ReadBufferLengh == -1) {
                        //重新归位到初始状态
                        RD = false;
                        Socket.close();
                        Socket = null;
                        buttontitle = true;
                        mainHandle.post(new Runnable() {
                            @Override
                            public void run() {
                                Button.setText("连 接 服 务 器");
                            }
                        });
                    }
                    else {
                        //先恢复成GB2312编码
                        textdata = new String(ReadBuffer,0,ReadBufferLengh,"UTF-8");//原始编码数据
                        StringBuilder sb = new StringBuilder();//string转ascii
                        //转为UTF-8编码后显示在编辑框中
                        Log.w("tag",textdata+"%"+textdata.length());
                        if (textdata.length()<=5){
                            strArr = textdata.split("\\|");
                        }
                        System.out.println(strArr[0]+"$"+strArr[1]);
                        mainHandle.post(new Runnable() {
                            @Override
                            public void run() {
                                temp.setText(strArr[0]);
                                humi.setText(strArr[1]);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //用线程发送数据
    class ThreadSendData extends Thread {
        private String IOT_AT;//效仿AT指令
        public ThreadSendData(String IOT_AT) {
            this.IOT_AT=IOT_AT;
        }
        public void run(){
            try {
                System.out.println(IOT_AT);
                //用输出流发送数据
                OutputStream.write(IOT_AT.getBytes());
                //发送数据之后会自动断开连接，所以，恢复为最初的状态
                //有个坑要说一下，因为发送完数据还得等待服务器返回，所以，不能把Socket也注销掉
                buttontitle = true;
                //改变按钮标题
                mainHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Button.setText("连 接 服 务 器");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
