package org.pushtalk.server.model;

import java.util.Date;

public class Message implements Comparable<Message> {

    String title;
    String content;
    String channel;
    Date time;
    
    public Message(String title, String content, String channel) {
        this.title = title;
        this.content = content;
        this.channel = channel;
        this.time = new Date();
    }

    @Override
    public int compareTo(Message o) {
        if (null == o) return -1;
        Message message = o;
        return this.time.compareTo(message.time);
    }
    
    @Override
    public boolean equals(Object o) {
        if (null == o) return false;
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return this.toString().equals(message.toString());
    }
    
    @Override
    public int hashCode() {
        return (int) time.getTime();
    }

    @Override
    public String toString() {
        return "Message - title:" + title + ", content=" + content
                + ", channel=" + channel + ", time=" + time;
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
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    
    
    
}
