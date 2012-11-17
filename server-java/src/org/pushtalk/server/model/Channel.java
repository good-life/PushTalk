package org.pushtalk.server.model;

import java.util.Date;

/**
 * 
 */
public class Channel {

	String name;
	int userCount;
	Date createTime;
	
	public Channel(String name) {
	    this.name = name;
	    this.userCount = 1;
	    this.createTime = new Date(); 
	}
	
	
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public int getUserCount() {
    	return userCount;
    }
	public void setUserCount(int userCount) {
    	this.userCount = userCount;
    }
	public Date getCreateTime() {
    	return createTime;
    }
	public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }
	
	
	
}
