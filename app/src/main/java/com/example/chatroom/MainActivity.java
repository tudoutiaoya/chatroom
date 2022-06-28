package com.example.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_cnt;
    private EditText et_ip;
    private EditText et_port;
    private EditText et_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_cnt = findViewById(R.id.btn_cnt);
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);
        et_name = findViewById(R.id.et_name);

        btn_cnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = et_name.getText().toString();
        if("".equals(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this,ChatRoomActivity.class);
            intent.putExtra("name", et_name.getText().toString());
            intent.putExtra("ip", et_ip.getText().toString());

            Log.d("zzq", "获取到的ip为" + et_ip.getText().toString());
            Log.d("zzq", "获取到的port为" + et_port.getText().toString());

            startActivity(intent);
        }
    }
}