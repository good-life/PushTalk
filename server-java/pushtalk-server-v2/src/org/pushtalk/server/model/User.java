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

public class User
{

    int id;
    String mail;
    String username;
    String userpwd;
    String nickname;
    String birthday;
    String signature;
    String gender;
    String region;

    static Logger LOG = Logger.getLogger(User.class);

    // generate a object
    public User()
    {
        super();
        id = 0; // 0 default as null
        mail = null;
        username = null;
        userpwd = null;
        nickname = null;
        birthday = null;
        signature = null;
        gender = null;
        region = null;
    }

    // insert user to database
    public int insertUser()
    {
        String sql = "INSERT INTO USER values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement st = null;
        try
        {
            st = H2Database.getInstance().getPreparedStatement(sql);
            st.setString(1, null); // id self increase
            st.setString(2, mail);
            st.setString(3, username);
            st.setString(4, userpwd);
            st.setString(5, nickname);
            st.setString(6, birthday);
            st.setString(7, signature);
            st.setString(8, gender);
            st.setString(9, region);

            return st.executeUpdate();
        } catch (SQLException e)
        {
            LOG.error("insert user error", e);
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
                LOG.error("insert user error", e);
                return 0;
            }
        }
    }

    @Override
    public String toString()
    {
        return "User [id=" + id + ", mail=" + mail + ", username=" + username + ", userpwd=" + userpwd + ", nickname=" + nickname + ", birthday=" + birthday
                + ", signature=" + signature + ", gender=" + gender + ", region=" + region + "]";
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUserpwd()
    {
        return userpwd;
    }

    public void setUserpwd(String userpwd)
    {
        this.userpwd = userpwd;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    // instance method start--------------------------------------------------
    public boolean isExist()
    {

        String sql = "select * from user where username = '" + username + "'";
        Statement st = null;
        try
        {
            st = H2Database.getInstance().getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next())
            {
                return true;
            } else
            {
                return false;
            }

        } catch (SQLException e)
        {
            LOG.error("find user error", e);
        } finally
        {
            Connection conn;
            try
            {
                conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("find user error", e);
            }

        }

        return false;
    }

    // instance method end--------------------------------------------------

    // factory method start--------------------------------------------------
    public static String getSCRIPTS_CREATE_TABLE()
    {
        return "CREATE TABLE USER (\n" + "  id int identity(1,1) PRIMARY KEY,\n" + "    mail VARCHAR (50),\n" + "   username VARCHAR (50),\n"
                + " userpwd VARCHAR (50),\n" + "    nickname VARCHAR (50),\n" + "   birthday VARCHAR (50),\n" + "   signature VARCHAR (50),\n"
                + " gender VARCHAR (50),\n" + " region VARCHAR (50)\n" + ")";
    }

    public static List<User> getAllUsers()
    {
        List<User> users = null;

        String sql = "select * from user";
        Statement st = null;
        try
        {
            st = H2Database.getInstance().getStatement();
            users = getUsersFromRS(st.executeQuery(sql));

        } catch (SQLException e)
        {
            LOG.error("insert user error", e);

        } finally
        {
            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                LOG.error("insert user error", e);
            }
        }

        return users;
    }

    public static List<User> getUsersFromRS(ResultSet rs) throws SQLException
    {
        List<User> users = new ArrayList<User>();
        User user = null;
        while (rs.next())
        {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setUserpwd(rs.getString("userpwd"));
            user.setMail(rs.getString("mail"));
            user.setBirthday(rs.getString("birthday"));
            user.setGender(rs.getString("gender"));
            user.setSignature(rs.getString("signature"));
            user.setNickname(rs.getString("nickname"));
            user.setRegion(rs.getString("region"));
            users.add(user);
        }
        return users;
    }
    // factory method end--------------------------------------------------

}
