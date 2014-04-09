package org.pushtalk.server.model;

import java.sql.Connection;
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
    String user_id;
    String friend_id;
    String alias;

    static Logger LOG = Logger.getLogger(Friendship.class);

    // generate a object
    public Friendship()
    {
        super();
    }

    @Override
    public String toString()
    {
        return "Friendship [id=" + id + ", user_id=" + user_id + ", friend_id=" + friend_id + ", alias=" + alias + "]";
    }

    public static String getSCRIPTS_CREATE_TABLE()
    {
        return "CREATE TABLE friendship (\n" + "	id int identity(1,1) PRIMARY KEY,\n" + "	user_id VARCHAR (50),\n" + "	friend_id VARCHAR (50),\n"
                + "	alias VARCHAR (50)\n" + ");" + "CREATE INDEX idx_friendship_id on friendship(id);"
                + "CREATE INDEX idx_friendship_user_id on friendship(user_id);" + "CREATE INDEX idx_friendship_friedn_id on friendship(friend_id);"
                + "CREATE INDEX idx_friendship_alias on friendship(alias);";
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getFriend_id()
    {
        return friend_id;
    }

    public void setFriend_id(String friend_id)
    {
        this.friend_id = friend_id;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    // instance method start--------------------------------------------------
    // instance method end--------------------------------------------------

    // factory method start--------------------------------------------------

    public static List<Friendship> getAllFriendshipBy(int user_id)
    {
        List<Friendship> allfs = null;

        String sql = "select * from friendship where user_id = " + user_id;
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
    // factory method end--------------------------------------------------

}
