package org.pushtalk.server.data.h2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.pushtalk.server.Config;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

public class H2Database {
    static Logger LOG = LoggerFactory.getLogger(H2Database.class);
    
    private static final String DB_PATH_FILE = "~/pushtalk";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";
    

    public List<Channel> getChannelListAll() {
    	String sql = "SELECT * FROM channel order by user_count desc"; 
    	
    	List<Channel> list = new ArrayList<Channel>();
        Statement st = null;
    	try {
    		st = getStatement();
    		ResultSet rs = st.executeQuery(sql);
    		while (rs.next()) {
    			list.add(fromResultSetChannel(rs));
    		}
	        
    	} catch (SQLException e) {
    		LOG.error("queryChannel error", e);
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
        return list;
    }
    
    public List<Channel> getChannelListByUdid(String udid) {
    	String sql = "SELECT c.* FROM channel c " +
    			"left join user_channel uc on uc.channel_name = c.name " +
    	        "where uc.udid = '" + udid + "'" +  
    			"order by user_count desc "; 
    	
    	List<Channel> list = new ArrayList<Channel>();
        Statement st = null;
    	try {
    		st = getStatement();
    		ResultSet rs = st.executeQuery(sql);
    		while (rs.next()) {
    			list.add(fromResultSetChannel(rs));
    		}
	        
    	} catch (SQLException e) {
    		LOG.error("get channel list by udid error", e);
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
        return list;
    }
    
    public Channel getChannelByName(String channelName) {
        String sql = "SELECT * FROM channel WHERE name = '" + channelName + "'"; 

        Channel channel = null;
        Statement st = null;
        try {
            st = getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                channel = fromResultSetChannel(rs);
            }
            
        } catch (SQLException e) {
            LOG.error("get channel by name error", e);
            
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
        return channel;
    }
    
    
    public int insertChannel(Channel channel) {
    	String sql = "INSERT INTO channel (name, user_count, createTime) values (?, ?, ?)";
    	PreparedStatement st = null;
    	try {
    		st = getPreparedStatement(sql);
    		st.setString(1, channel.getName());
    		st.setInt(2, channel.getUserCount());
    		st.setTimestamp(3, new Timestamp(channel.getCreateTime().getTime()));
    		return st.executeUpdate();
    	} catch (Exception e) {
    		LOG.error("insertChannel error", e);
    		return -1;
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public int updateChannelUserCount(String name, int step) {
    	String sql = "UPDATE channel SET user_count = user_count + " + step;
    	sql += " WHERE name = '" + name + "'";
    	
        Statement st = null;
    	try {
    		st = getStatement();
    		return st.executeUpdate(sql);
    	} catch (Exception e) {
    		LOG.error("update channel user count error", e);
    		return -1;
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public boolean deleteChannel(String name) {
    	String sql = "DELETE from channel WHERE name = '" + name + "'";
    	
        Statement st = null;
    	try {
    		st = getStatement();
	        st.executeUpdate(sql);
	        return true;
	        
    	} catch (Exception e) {
    		LOG.error("delete Channel error", e);
    		return false;
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public List<User> getUserListByChannel(String channelName) {
    	String sql = "SELECT u.* FROM user u left join user_channel uc on u.udid = uc.udid" +
    			" where uc.channel_name = '" + channelName + "'" + 
    			" order by u.createTime desc ";
    	
    	List<User> list = new ArrayList<User>();
        Statement st = null;
    	try {
    		st = getStatement();
    		ResultSet rs = st.executeQuery(sql);
    		while (rs.next()) {
    			list.add(fromResultSetUser(rs));
    		}
    		return list;
	        
    	} catch (Exception e) {
    		LOG.error("get user list by channel error", e);
    		return list;
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public User getUserByUdid(String udid) {
        String sql = "SELECT * from user where udid = '" + udid + "'";
        Statement st = null;
        User user = null;
        try {
            st = getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                user = fromResultSetUser(rs);
            }
        } catch (SQLException e) {
            LOG.error("query user error", e);
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
        
        return user;
    }
    
    public User getUserByName(String name) {
        String sql = "SELECT * from user where name = '" + name + "'";
        Statement st = null;
        User user = null;
        try {
            st = getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                user = fromResultSetUser(rs);
            }
        } catch (SQLException e) {
            LOG.error("query user error", e);
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
        
        return user;
    }
    
    public int insertUser(User user) {
    	String sql = "INSERT INTO user (udid, name, createTime) values (?, ?, ?)";
        PreparedStatement st = null;
    	try {
    		st = getPreparedStatement(sql);
    		st.setString(1, user.getUdid());
    		st.setString(2, user.getName());
    		st.setTimestamp(3, new Timestamp(user.getCreateTime().getTime()));
	        return st.executeUpdate();
    	} catch (SQLException e) {
    		LOG.error("insert user error", e);
    		return -1;
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public boolean deleteUser(String udid) {
    	String sql = "DELETE from user WHERE udid = '" + udid + "'";
    	
        Statement st = null;
    	try {
    		st = getStatement();
	        st.executeUpdate(sql);
	        return true;
	        
    	} catch (Exception e) {
    		LOG.error("delete user error", e);
    		return false;
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public int updateUserName(User newUser) {
        String sql = "UPDATE user set name = ? where udid = ?";
        
        PreparedStatement st = null;
        try {
            st = getPreparedStatement(sql);
            st.setString(1, newUser.getName());
            st.setString(2, newUser.getUdid());
            return st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("update user name error", e);
            return -1;
            
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }            
        }
    }
    
    
    public boolean isUserInChannel(String udid, String channelName) {
        String sql = "SELECT * from user_channel where udid = '" + udid + "' AND channel_name = '" + channelName + "'";
        Statement st = null;
        try {
            st = getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("is user in channel", e);
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
        
        return false;
    }
    
    public int insertUserChannel(String udid, String channelName) {
        String sql = "INSERT INTO user_channel (udid, channel_name) values (?, ?)";
        
        PreparedStatement st = null;
        try {
            st = getPreparedStatement(sql);
            st.setString(1, udid);
            st.setString(2, channelName);
            return st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("update user_channel error", e);
            return -1;
            
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }            
        }
    }
    
    public int deleteUserChannel(String udid, String channelName) {
        String sql = "DELETE from user_channel WHERE udid = '" + udid + "' AND channel_name = '" + channelName + "'";
        
        Statement st = null;
        try {
            st = getStatement();
            return st.executeUpdate(sql);
            
        } catch (Exception e) {
            LOG.error("delete user_channel error", e);
            return -1;
            
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
    

    
    
    
    // ------------------------------------- common
    
    private H2Database() {
    	// avoid to be created directly
    }
    
    private static H2Database h2Database = null;
    public static H2Database getInstance() {
    	if (null == h2Database) {
    		h2Database = new H2Database();
    	}
    	return h2Database;
    }
    
    
    public void init() throws SQLException {
    	LOG.info("Begin to init H2 database.");
    	
		try {
	        Class.forName("org.h2.Driver");
		} catch (Exception e) {
			LOG.error("h2 database driver not found. Please ensure the driver jar is in classpath.");
			throw new SQLException("h2 database driver not found");
		}
		
		
        Statement st = null;
    	try {
            _dataSource = createDriudDataSource();
            
            
    		st = getStatement();
    		Connection conn = st.getConnection();
    		DatabaseMetaData meta = conn.getMetaData();
    		ResultSet rs = meta.getTables(null, null, null, new String[] {"TABLE"});
    		ArrayList<String> tableList = new ArrayList<String>();
    		while (rs.next()) {
    			tableList.add(rs.getString("TABLE_NAME"));
    		}
    		LOG.debug("Exist tables - " + tableList.size() + " - " + tableList.toString());
    		
    		if (!tableList.contains("CHANNEL")) {
    			LOG.info("Create table - channel");
    	        st.execute(SCRIPTS_CREATE_TABLE_CHANNEL);
    		}
    		
    		if (!tableList.contains("USER")) {
    			LOG.info("Create table - user");
    	        st.execute(SCRIPTS_CREATE_TABLE_USER);
    		}

    		if (!tableList.contains("USER_CHANNEL")) {
    			LOG.info("Create table - user_channel");
    	        st.execute(SCRIPTS_CREATE_TABLE_USER_CHANNEL);
    		}

            if (!tableList.contains("SERVER_INFO")) {
                LOG.info("Create table - server_info");
                st.execute(SCRIPTS_CREATE_TABLE_SERVER_INFO);
            }
            
            String sql = "SELECT * from server_info";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                Config.SERVER_ID = rs.getString(1);
            } else {
                String generated = StringUtils.getRandomString().substring(0, 8);
                Config.SERVER_ID = generated;
                sql = "INSERT INTO server_info (server_id) values ('" + generated + "')";
                int ret = st.executeUpdate(sql);
                if (ret <=0) {
                    LOG.error("Unexcepiton: Insert server_id error.");
                    throw new SQLException("Unexcepiton: Insert server_id error.");
                }
            }
            LOG.info("The server_id: " + Config.SERVER_ID);
    		
    	} catch (SQLException e) {
    	    LOG.error("Init error", e);
    	    e.printStackTrace();
    	    throw new SQLException("Init error");

		} finally {
		    if (null == st) return;
		    
			try {
	    		Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    

    
    private Statement getStatement() throws SQLException {
		Connection connection = _dataSource.getConnection();
		if (connection == null){
			throw new SQLException("Unexpected: cannot get connection from pool");
		}
        return connection.createStatement();
    }
    
    private PreparedStatement getPreparedStatement(String sql) throws SQLException {
		Connection connection = _dataSource.getConnection();
		if (connection == null){
			throw new SQLException("Unexpected: cannot get connection from pool");
		}
        return connection.prepareStatement(sql);
    }
    
    private static DruidDataSource _dataSource;
    public static DruidDataSource createDriudDataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:" + DB_PATH_FILE);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        
        dataSource.setMaxActive(20);
        
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(50);
                
        dataSource.setTestWhileIdle(false);
        
        LOG.info("Datasource params - " + dataSource.getUsername() + ", "
                + dataSource.getPassword() + ", " + dataSource.getUrl());
        
        return dataSource;
    }
    
    private Channel fromResultSetChannel(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
    	Channel channel = new Channel(name);
    	channel.setUserCount(rs.getInt("user_count"));
    	channel.setCreateTime(rs.getDate("createTime"));
    	return channel;
    }
    
    private User fromResultSetUser(ResultSet rs) throws SQLException {
        String udid = rs.getString("udid");
        String name = rs.getString("name");
    	User user = new User(udid, name);
    	user.setCreateTime(rs.getDate("createTime"));
    	return user;
    }
    
    public static String unescape(String obj) {
    	return obj.replace("\n", "<br/>");
    }


    
    private static final String SCRIPTS_CREATE_TABLE_SERVER_INFO =  
            "CREATE TABLE server_info ("  + 
                "server_id VARCHAR(50)" +
                "); ";
    
    
    private static final String SCRIPTS_CREATE_TABLE_CHANNEL =  
    		"CREATE TABLE channel ("  + 
    			"user_count INT," +
    			"name VARCHAR(100)," +
    			"createTime DATETIME " +
    			"); " +
    		"CREATE INDEX idx_channel_name on channel(name);" +
            "CREATE INDEX idx_channel_createTime on channel(createTime);";
    
    private static final String SCRIPTS_CREATE_TABLE_USER =  
    		"CREATE TABLE user (" +
        		"udid VARCHAR(50), " +
    			"name VARCHAR(100)," +
    			"createTime DATETIME " +
    			");" + 
            "CREATE INDEX idx_user_udid on user(udid);" + 
            "CREATE INDEX idx_user_name on user(name);" + 
            "CREATE INDEX idx_user_createTime on user(createTime);";
    
    private static final String SCRIPTS_CREATE_TABLE_USER_CHANNEL =  
    		"CREATE TABLE user_channel (" +
    			"udid VARCHAR(50), " + 
    			"channel_name VARCHAR(100)," +
    			");" + 
            "CREATE INDEX idx_uc_udid on user_channel(udid);" +  
            "CREATE INDEX idx_uc_channel_name on user_channel(channel_name);";
    

}



