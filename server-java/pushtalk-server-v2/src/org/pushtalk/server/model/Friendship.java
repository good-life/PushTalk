package org.pushtalk.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pushtalk.server.data.h2.H2Database;

public class Friendship
{
    String id;
    String user_name;
    String friend_name;
    String alias;

    static Logger LOG = Logger.getLogger(Friendship.class);

    // generate a object
    public Friendship()
    {
        super();
    }

    public static String getSCRIPTS_CREATE_TABLE()
    {
        return "CREATE TABLE friendship (\n" + "	id int identity(1,1) PRIMARY KEY,\n" + "	user_name VARCHAR (50),\n" + "	friend_name VARCHAR (50),\n"
                + "	alias VARCHAR (50)\n" + ");";
    }

    // instance method start--------------------------------------------------
    // instance method end--------------------------------------------------

    // factory method start--------------------------------------------------

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public String getFriend_name()
    {
        return friend_name;
    }

    public void setFriend_name(String friend_name)
    {
        this.friend_name = friend_name;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public static List<Friendship> getAllFriendshipBy(String user_name)
    {
        List<Friendship> allfs = null;

        String sql = "select * from friendship where user_name = '" + user_name + "'";
        Statement st = null;
        try
        {
            st = H2Database.getInstance().getStatement();
            allfs = getFriendshipsFromRS(st.executeQuery(sql));

        } catch (SQLException e)
        {
            LOG.error("select friendships with userid error", e);

        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("select friendships with userid error", e);
            }
        }
        return allfs;

    }

    public static List<Friendship> getFriendshipsFromRS(ResultSet rs) throws SQLException
    {
        List<Friendship> fss = new ArrayList<Friendship>();
        Friendship fs = null;
        while (rs.next())
        {
            fs = new Friendship();

            fss.add(fs);
        }
        return fss;
    }

    public static int addFriendship(String user_name, String friend_name)
    {

        String sql = "INSERT INTO FRIENDSHIP values (?,?,?,?)";
        PreparedStatement st = null;
        try
        {
            st = H2Database.getInstance().getPreparedStatement(sql);
            st.setString(1, null);
            st.setString(2, user_name);
            st.setString(3, friend_name);
            st.setString(4, "");
            return st.executeUpdate();
        } catch (SQLException e)
        {
            LOG.error("insert friendship error", e);
            return 0;
        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("insert friendship error", e);
                return 0;
            }
        }
    }

    public static int deleteFriendship(String user_name, String friend_name)
    {

        String sql = "DELETE FROM FRIENDSHIP WHERE USER_NAME=? AND FRIEND_NAME=?";
        PreparedStatement st = null;
        try
        {
            st = H2Database.getInstance().getPreparedStatement(sql);
            st.setString(1, user_name);
            st.setString(2, friend_name);
            return st.executeUpdate();
        } catch (SQLException e)
        {
            LOG.error("delete friendship error", e);
            return 0;
        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("delete friendship error", e);
                return 0;
            }
        }
    }
    
    public static String getAlias(String user_name, String friend_name)
    {
        //to do

        String sql = "SELECT ALIAS FROM FRIENDSHIP WHERE USER_NAME=? AND FRIEND_NAME=?";
        PreparedStatement st = null;
        try
        {
            st = H2Database.getInstance().getPreparedStatement(sql);
            st.setString(1, user_name);
            st.setString(2, friend_name);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getString("ALIAS");
            }
            return null;
        } catch (SQLException e)
        {
            LOG.error("delete friendship error", e);
            return null;
        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("delete friendship error", e);
                return null;
            }
        }
    }
    
    public static int setAlias(String user_name, String friend_name, String alias)
    {

        String sql = "UPDATE FRIENDSHIP SET ALIAS=? WHERE USER_NAME=? AND FRIEND_NAME=?";
        PreparedStatement st = null;
        try
        {
            st = H2Database.getInstance().getPreparedStatement(sql);
            st.setString(1, alias);
            st.setString(2, user_name);
            st.setString(3, friend_name);
            return st.executeUpdate();
        } catch (SQLException e)
        {
            LOG.error("update alias error", e);
            return 0;
        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("update alias error", e);
                return 0;
            }
        }
    }
    
    // factory method end--------------------------------------------------

}
