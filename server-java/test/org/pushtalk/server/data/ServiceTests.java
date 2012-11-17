package org.pushtalk.server.data;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pushtalk.server.data.h2.H2Database;
import org.pushtalk.server.data.h2.TalkServiceImpl;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;

public class ServiceTests {
    
    static TalkService talkService;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (null == talkService) {
            try {
                talkService = new TalkServiceImpl();
            } catch (SQLException e) {
                Assert.fail("Cannot init");
            }
        }
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testChannelGetChannelByName() {
        String channelName = "test_channel";
        Channel channel = new Channel(channelName);
        H2Database.getInstance().insertChannel(channel);
        
        Channel existed = talkService.getChannelByName(channelName);
        Assert.assertEquals(channelName, existed.getName());
        
        H2Database.getInstance().deleteChannel(channelName);
        existed = talkService.getChannelByName(channelName);
        Assert.assertNull(existed);
    }
    
    @Test
    public void testGetChannelListAll() {
        Channel channel_1 = new Channel("test 1");
        Channel channel_2 = new Channel("test 2");
        H2Database.getInstance().insertChannel(channel_1);
        H2Database.getInstance().insertChannel(channel_2);
        
        List<Channel> list = talkService.getChannelListAll();
        Assert.assertEquals(2, list.size());
        
        H2Database.getInstance().deleteChannel(channel_1.getName());
        H2Database.getInstance().deleteChannel(channel_2.getName());
    }
    
    @Test
    public void testRegisterUser() {
        String udid = "4312431241234123";
        String userName = "Javen";
        talkService.registerUser(udid, userName);
        
        User user = talkService.getUserByUdid(udid);
        Assert.assertNotNull(user);
        
        user = talkService.getUserByName(userName);
        Assert.assertNotNull(user);
        
        H2Database.getInstance().deleteUser(udid);
        user = talkService.getUserByUdid(udid);
        Assert.assertNull(user);
    }
    
    @Test
    public void testGetUserListByChannel() {
        String channelName = "test channel";
        
        String udid = "4312431241234123";
        String userName = "Javen";
        talkService.registerUser(udid, userName);
        
        talkService.enterChannel(channelName, udid);
        List<User> list = talkService.getUserListByChannel(channelName);
        Assert.assertEquals(1, list.size());
        
        talkService.exitChannel(channelName, udid);
        list = talkService.getUserListByChannel(channelName);
        Assert.assertEquals(0, list.size());
        
        List<Channel> clist = talkService.getChannelListAll();
        Assert.assertEquals(0, clist.size());
        
        User user = talkService.getUserByUdid(udid);
        Assert.assertNotNull(user);
        
        H2Database.getInstance().deleteUser(udid);
        user = talkService.getUserByUdid(udid);
        Assert.assertNull(user);
    }
    

    

}
