package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.adapter.MsgAdapter;
import com.example.chatroom.database.MsgDatabase;
import com.example.chatroom.entity.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<>();
    private Button back;
    private String name;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private RecyclerView msgRecyclerView;
    private boolean isSend;
    private MsgDatabase msgDatabase;
    private Socket socketSend;
    private String ip = "192.168.1.104";
    private String port = "6666";
    boolean isRunning = false;
    DataInputStream dis;
    DataOutputStream dos;
    String recMsg = "";

    private Handler handler = new Handler(Looper.myLooper()){//获取当前进程的Looper对象传给handler
        @Override
        public void handleMessage(@NonNull Message msg){//?
            if(!recMsg.isEmpty()){
                addNewMessage(recMsg,Msg.TYPE_RECEIVED);//添加新数据
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        //获取意图
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);


        send.setOnClickListener(this);

        //初始化数据库  开启一个线程  耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                msgDatabase = MsgDatabase.getInstance(ChatRoomActivity.this);

                //读取数据库
                initData();
                Log.d("zzq", "初始化完毕");
            }
        },"初始化").start();


        //返回逻辑
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatRoomActivity.this);
                dialog.setTitle("退出");
                dialog.setMessage("退出登录?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); //结束当前Activity声明周期
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


        //测试连接并启动发送线程  接收线程
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                        socketSend = new Socket(ip, Integer.parseInt(port));
                        isRunning = true;
                        Log.d("zzq","发送了一条消息2");
                        dis = new DataInputStream(socketSend.getInputStream());
                        dos = new DataOutputStream(socketSend.getOutputStream());
                    new Thread(new receive(), "接收线程").start();
                    new Thread(new Send(),"发送线程").start();


                }catch(Exception e){
                    isRunning = false;
                    e.printStackTrace();
                    System.err.println("失败原因：" + e);
                    //让子线程显示Toast
                    Looper.prepare();
                    Toast.makeText(ChatRoomActivity.this, "连接服务器失败！！！" + e, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    try{
                        socketSend.close();
                    }catch(IOException e1){
                        e1.printStackTrace();
                    }
                    finish();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        String content = inputText.getText().toString();
        if ("".equals(content.trim())) {
            Toast.makeText(this ,"请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        addNewMessage(content, Msg.TYPE_SENT);

        //自动回复
        addNewMessage("对方不在线", Msg.TYPE_RECEIVED);

        isSend = true;
        //清空input
        inputText.setText("");
    }

    private void addNewMessage(String msg, int type) {
        Date now = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowData = dataformat.format(now);
        Msg message = new Msg(msg,type, nowData);
        msgList.add(message);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);

        //开启一个子线程保存到数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                msgDatabase.msgDao().insertMsg(message);
            }
        }, "保存数据库").start();
    }

    //初始化数据
    private void initData() {
        List<Msg> msgs = msgDatabase.msgDao().queryAll();
//        System.out.println(msgs);
        msgList = msgs;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);

                msgRecyclerView= findViewById(R.id.msg_recycler_view);
                msgRecyclerView.setLayoutManager(layoutManager);

                adapter = new MsgAdapter(msgList);
                msgRecyclerView.setAdapter(adapter);

                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });
    }

    class receive implements Runnable{
        public void run(){
            recMsg = "";
            while(isRunning){
                Log.e("zzq", "接收线程启动了");

                try{
                    recMsg = dis.readUTF();

                    Log.d("zzq","收到了一条消息"+"recMsg: "+ recMsg);

                    Looper.prepare();
                    Toast.makeText(ChatRoomActivity.this,"收到了一条消息"+"recMsg: "+ recMsg, Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }catch(Exception e){
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(recMsg)){
                    Log.d("zzq","inputStream:"+dis);
                    Message message = new Message();
                    message.obj=recMsg;
                    handler.sendMessage(message);
                }
            }
        }
    }

    class Send implements Runnable{
        @Override
        public void run(){
            while(isRunning){

                String content = inputText.getText().toString();
                if(!"".equals(content)&&isSend){
                    @SuppressLint("SimpleDateFormat")
                    String date = new SimpleDateFormat("hh:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append(content).append("\n\n来自：").append(name).append("\n"+date);
                    content = sb.toString();
                    try{
                        dos.writeUTF(content);
                        dos.flush();
                        sb.delete(0,sb.length());
                        Log.d("ttw","发送了一条消息");
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    isSend = false;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inputText.setText("");
                        }
                    });
                }
            }
        }
    }

}