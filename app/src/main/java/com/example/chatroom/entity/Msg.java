package com.example.chatroom.entity;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private int id;
    private String content;
    private int type;
    private String data;

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
