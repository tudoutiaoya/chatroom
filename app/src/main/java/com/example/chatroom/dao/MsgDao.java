package com.example.chatroom.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.chatroom.entity.Msg;

import java.util.List;

@Dao
public interface MsgDao {
    @Insert
    void insertMsg(Msg msg);

    @Query("select * from chat_room")
    List<Msg> queryAll();
}
