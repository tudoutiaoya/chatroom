package com.example.chatroom.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.chatroom.dao.MsgDao;
import com.example.chatroom.entity.Msg;

@Database(entities = {Msg.class}, version = 1, exportSchema = true)
public abstract class MsgDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "chat_room";

    private static MsgDatabase msgDatabaseInstance;

    //单例模式
    public static synchronized MsgDatabase getInstance(Context context)
    {
        if(msgDatabaseInstance == null)
        {
            msgDatabaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), MsgDatabase.class, DATABASE_NAME)
                    .build();
        }
        return msgDatabaseInstance;
    }

    public abstract MsgDao msgDao();

}
