package org.pushtalk.server.model;

import java.util.Date;

import com.google.gson.Gson;

public class Message implements Comparable<Message> {
    
	int id;
    String title;
    String content;
    String channel;
    long time;
    
    //是否已读
    //boolean readable;
    
    public Message(int id, String title, String content, String channel) {
    	this.id = id;
        this.title = title;
        this.content = content;
        this.channel = channel;
        this.time = new Date().getTime();
    }
    
    @Override
    public int compareTo(Message o) {
        if (null == o) return -1;
        if (o.id == this.id) return 0;
        if (o.id > this.id) return -1;
        else return 1;
    }
    
    @Override
    public boolean equals(Object o) {
        if (null == o) return false;
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return this.id == message.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
    public int getId() {
    	return id;
    }
    public void setId(int id) {
    	this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}

