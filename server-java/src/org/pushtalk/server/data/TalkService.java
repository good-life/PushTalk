package org.pushtalk.server.data;

import java.util.List;
import java.util.Set;

import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.Message;
import org.pushtalk.server.model.RecentChat;
import org.pushtalk.server.model.User;

public interface TalkService {
    
    // --------------- chatting
    
    void putMessage(String udid, String chatting, Message message);
    
    void newRecentChat(String udid, String chatting);
    
    void showedMessage(String udid, String chatting);
    
    void unreadMessage(String udid, String chatting);
    
    Set<Message> getRecentMessages(String chatting);
    
    Set<RecentChat> getRecentChats(String udid);

    // --------------- channel 
    
	List<Channel> getChannelListAll();
	
	Channel getChannelByName(String channelName);
	
	boolean createChannel(String channelName, String udid);
	
	boolean enterChannel(String channelName, String udid);
	
	boolean exitChannel(String channelName, String udid);
	
	// ---------------- user
	
	User getUserByUdid(String udid);
	
	User getUserByName(String name);
	
	boolean registerUser(String udid, String userName);
	
	List<User> getUserListByChannel(String channelName);
	
	
}
