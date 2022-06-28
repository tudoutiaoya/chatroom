package com.example.chatroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.adapter.MsgAdapter;
import com.example.chatroom.database.MsgDatabase;
import com.example.chatroom.entity.Msg;

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

        //初始化 recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoomActivity.this);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        Msg msg = new Msg("快和我聊天吧", Msg.TYPE_RECEIVED, "2022-6-29");
        msgList.add(msg);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);

        send.setOnClickListener(this);

        //初始化数据库  开启一个线程  耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                msgDatabase = MsgDatabase.getInstance(ChatRoomActivity.this);


                Log.d("zzq", "初始化完毕");
            }
        }).start();


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


}