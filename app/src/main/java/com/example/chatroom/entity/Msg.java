package com.example.chatroom.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_room")
public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    private int id;
    @ColumnInfo(name = "content", typeAffinity = ColumnInfo.TEXT)
    private String content;
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.INTEGER)
    private int type;
    @ColumnInfo(name = "data", typeAffinity = ColumnInfo.TEXT)
    private String data;

    @Ignore
    public Msg(String content, int type, String data) {
        this.content = content;
        this.type = type;
        this.data = data;
    }

    public Msg(int id, String content, int type, String data) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}
