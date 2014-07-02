package org.pushtalk.server.data.h2;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.pushtalk.server.Config;
import org.pushtalk.server.data.TalkService;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.Message;
import org.pushtalk.server.model.RecentChat;
import org.pushtalk.server.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;

public class TalkServiceImpl implements TalkService {
    static Logger LOG = LoggerFactory.getLogger(TalkServiceImpl.class);

    // key: chatting channel;    Cache: message
    private final Map<String, Cache<Message, String>> mMessagesMap;
    
    // key: udid;    Cache: RecentChat
    private final Map<String, Cache<String, RecentChat>> mRecentChatMap;

    
	public TalkServiceImpl() throws SQLException {
		H2Database.getInstance().init();
		LOG.info("h2 database init finished.");
		
		mMessagesMap = new HashMap<String, Cache<Message, String>>();
		mRecentChatMap = new HashMap<String, Cache<String, RecentChat>>();
	}
	
	public void unreadMessage(String udid, String chatting) {
        Cache<String, RecentChat> chatCache = getRecentChatCache(udid);
        RecentChat existed = chatCache.getIfPresent(chatting);
        if (existed != null) {
            existed.increaseUnread();
        } else {
            RecentChat chat = new RecentChat(chatting);
            chat.increaseUnread();
            chatCache.put(chatting, chat);
        }
	}
	
    public void showedMessage(String udid, String chatting) {
        Cache<String, RecentChat> chatCache = getRecentChatCache(udid);
        RecentChat existed = chatCache.getIfPresent(chatting);
        if (existed != null) {
            existed.clearUnread();
        } else {
            RecentChat chat = new RecentChat(chatting);
            chat.clearUnread();
            chatCache.put(chatting, chat);
        }
    }
    
	public void putMessage(String udid, String chatting, Message message) {
	    Cache<Message, String> messageCache = getMessageCache(chatting);
	    messageCache.put(message, "");
	}
	
    public void newRecentChat(String udid, String chatting) {
        LOG.debug("newRecentChat chatting - " + chatting);
        Cache<String, RecentChat> chatCache = getRecentChatCache(udid);
        RecentChat existed = chatCache.getIfPresent(chatting);
        if (existed != null) {
            existed.increaseUnread();
        } else {
            RecentChat chat = new RecentChat(chatting);
            chatCache.put(chatting, chat);
        }
    }

	
    public Set<Message> getRecentMessages(String channel) {
        Cache<Message, String> cache = getMessageCache(channel);
        Set<Message> originalSet = cache.asMap().keySet();
        return getOrderedMessages(originalSet);
    }
    
    private Set<Message> getOrderedMessages(Set<Message> originalSet) {
        TreeSet<Message> ordered = Sets.newTreeSet(originalSet);
        
        return ordered;
    }
    
    private Cache<Message, String> getMessageCache(String chatting) {
        Cache<Message, String> cache = null;
        if (mMessagesMap.containsKey(chatting)) {
            cache = mMessagesMap.get(chatting);
            
        } else {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(Config.MESSAGE_CACHE_MAX_NUMBER)
                    .expireAfterAccess(Config.CACHE_DURATION_DAYS, TimeUnit.DAYS)
                    .build();
            mMessagesMap.put(chatting, cache);
        }
        return cache;
    }
    
    public Set<RecentChat> getRecentChats(String udid) {
        Cache<String, RecentChat> cache = getRecentChatCache(udid);
        
        Map<String, RecentChat> map = cache.asMap();
        TreeSet<RecentChat> treeSet = Sets.newTreeSet();
        for (RecentChat rc : map.values()) {
            treeSet.add(rc);
        }
        return treeSet.descendingSet();
    }
    
    private Cache<String, RecentChat> getRecentChatCache(String udid) {
        Cache<String, RecentChat> cache = null;
        if (mRecentChatMap.containsKey(udid)) {
            cache = mRecentChatMap.get(udid);
            
        } else {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(Config.RECENT_CHATS_CACHE_MAX_NUMBER)
                    .expireAfterAccess(Config.CACHE_DURATION_DAYS, TimeUnit.DAYS)
                    .build();
            mRecentChatMap.put(udid, cache);
        }
        return cache;
    }
    
    
	
	@Override
	public List<Channel> getChannelListAll() {
		return H2Database.getInstance().getChannelListAll();
	}
	
	@Override
	public Channel getChannelByName(String channelName) {
	    return H2Database.getInstance().getChannelByName(channelName);
	}

    @Override
    public boolean createChannel(String channelName, String udid) {
        Channel channel = new Channel(channelName);
        H2Database.getInstance().insertChannel(channel);
        H2Database.getInstance().insertUserChannel(udid, channelName);
        return true;
    }

	@Override
	public boolean enterChannel(String channelName, String udid) {
        boolean alreadyIn = H2Database.getInstance().isUserInChannel(udid, channelName);
        if (!alreadyIn) {
            H2Database.getInstance().insertUserChannel(udid, channelName);
            H2Database.getInstance().updateChannelUserCount(channelName, 1);
            return true;
        } else {
            return false;
        }
	}

	@Override
	public boolean exitChannel(String channelName, String udid) {
		int deletedUserChannel = H2Database.getInstance().deleteUserChannel(udid, channelName);
		if (deletedUserChannel > 0) {
		    H2Database.getInstance().updateChannelUserCount(channelName, -1);
		    Channel existedChannel = H2Database.getInstance().getChannelByName(channelName);
		    if (existedChannel.getUserCount() == 0) {
		        LOG.info("The channel now has 0 memeber. Delete it - " + channelName);
		        H2Database.getInstance().deleteChannel(channelName);
		    }
		    return true;
		} else {
		    LOG.warn("Delete user channel failed.");
		}
		return false;
	}
	

	@Override
	public User getUserByUdid(String udid) {
		User user = H2Database.getInstance().getUserByUdid(udid);
		if (null == user) return null;
		
		List<Channel> channelList = H2Database.getInstance().getChannelListByUdid(udid);
		user.setChannelList(channelList);
		return user;
	}

    @Override
    public User getUserByName(String name) {
        return H2Database.getInstance().getUserByName(name);
    }
    
	@Override
	public boolean registerUser(String udid, String userName) {
	    User user = H2Database.getInstance().getUserByUdid(udid);
	    if (null != user) {
	        user.setName(userName);
	        int ret = H2Database.getInstance().updateUserName(user);
	        return (ret > 0 ? true : false);
	    } else {
	        user = new User(udid, userName);
	        int ret = H2Database.getInstance().insertUser(user);
	        return (ret > 0 ? true : false);
	    }
	}

	@Override
	public List<User> getUserListByChannel(String channelName) {
	    return H2Database.getInstance().getUserListByChannel(channelName);
	}

	

}
