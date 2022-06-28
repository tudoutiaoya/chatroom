package com.example.chatroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private String name;
    private EditText inputText;
    private Button send;

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
        }
    }
}