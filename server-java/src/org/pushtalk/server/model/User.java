package org.pushtalk.server.model;

import java.util.Date;
import java.util.List;

/**
 * udid -> User Device ID.  On Android, we use imei
 */
public class User {

	String udid;
	String name;
	Date createTime;
	
	List<Channel> channelList;
	
	public User(String udid, String name) {
	    this.udid = udid;
	    this.name = name;
	    this.createTime = new Date();
	}
	
	
	@Override
    public String toString() {
		return "User - udid:" + udid + ", name:" + name;
	}
	
	public List<Channel> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<Channel> channelList) {
	    this.channelList = channelList;
	}
	
	
	
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }

	public String getUdid() {
    	return udid;
    }

	public void setUdid(String udid) {
    	this.udid = udid;
    }

	public Date getCreateTime() {
    	return createTime;
    }

	public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

	
}
