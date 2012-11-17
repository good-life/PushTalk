package org.pushtalk.server.utils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceUtilsTests {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String myName = "方";
        String friendName = "家";
        String channel = ServiceUtils.getChattingChannel(myName, friendName);
        Assert.assertEquals("@家@方", channel);
    }
    
    @Test
    public void testIsValidAliasAndTags() {
        String s = "ffdsafdsakl ";
        Assert.assertEquals(true, ServiceUtils.isValidAliasOrTag(s));
    }

}

