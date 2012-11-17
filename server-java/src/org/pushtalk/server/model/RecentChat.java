package org.pushtalk.server.model;

import java.util.Calendar;
import java.util.Date;

import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.utils.StringUtils;

public class RecentChat implements Comparable<RecentChat> {

    String chatting;
    boolean isChannel;
    int unReadNumber;
    Date lastUpdated;
    
    public RecentChat(String chatting) {
        this.chatting = chatting;
        if (chatting.startsWith(ServiceUtils.CHANNEL_PREFIX)) {
            this.isChannel = true;
        } else {
            this.isChannel = false;
        }
        this.lastUpdated = new Date();
        this.unReadNumber = 1;
    }
    
    public void clearUnread() {
        this.unReadNumber = 0;
    }
    
    public void increaseUnread() {
        this.unReadNumber ++;
        this.lastUpdated = new Date();
    }
    
    public boolean isToday() {
        Calendar now = Calendar.getInstance();
        Calendar updated = Calendar.getInstance();
        updated.setTimeInMillis(lastUpdated.getTime());
        return (now.get(Calendar.DAY_OF_YEAR) == updated.get(Calendar.DAY_OF_YEAR));
    }
    
    public String getChatChannel() {
        if (chatting != null && isChannel) {
            return chatting.substring(1, chatting.length());
        }
        return "";
    }
    
    public String getChatFriend(final String myName) {
        if (chatting != null && !isChannel) {
            String friend = chatting.substring(1, chatting.length());
            String[] array = friend.split(ServiceUtils.USER_PREFIX);
            if (array[0].equals(myName)) {
                friend = array[1];
            } else {
                friend = array[0];
            }
            return friend;
        }
        return "";
    }
    
    public String getChatTarget(String channelName, String friend) {
        if (!StringUtils.isTrimedEmpty(channelName)) {
            return ServiceUtils.CHANNEL_PREFIX + channelName;
        } else {
            return ServiceUtils.USER_PREFIX + friend;
        }
    }
    
    @Override
    public int compareTo(RecentChat o) {
        if (null == o) return -1;
        RecentChat rc = o;
        return this.lastUpdated.compareTo(rc.lastUpdated);
    }
    
    @Override
    public boolean equals(Object o) {
        if (null == o) return false;
        if (!(o instanceof RecentChat)) {
            return false;
        }
        RecentChat rc = (RecentChat) o;
        return this.toString().equals(rc.toString());
    }
    
    @Override
    public int hashCode() {
        return (int) lastUpdated.getTime();
    }
    
    @Override
    public String toString() {
        return "RecentChat - chatting:" + chatting
                + ", isChannel:" + isChannel + ", unReadNumber:" + unReadNumber
                + ", lastUpdateTime" + lastUpdated;
    }
    
    
    public String getChatting() {
        return chatting;
    }
    public void setChatting(String chatting) {
        this.chatting = chatting;
    }
    public boolean isChannel() {
        return isChannel;
    }
    public void setChannel(boolean isChannel) {
        this.isChannel = isChannel;
    }
    public int getUnReadNumber() {
        return unReadNumber;
    }
    public void setUnReadNumber(int unReadNumber) {
        this.unReadNumber = unReadNumber;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    
}
