package org.pushtalk.server.data.h2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Friendship;
import org.pushtalk.server.model.User;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class H2Database
{
    static Logger LOG = Logger.getLogger(H2Database.class);

    private static final String DB_PATH_FILE = "~/pushtalk/pushtalk";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";

    // ------------------------------------- common

    private H2Database()
    {
        // avoid to be created directly
    }

    private static H2Database h2Database = null;

    public static H2Database getInstance()
    {
        if (null == h2Database)
        {
            h2Database = new H2Database();
        }
        return h2Database;
    }

    public void init() throws SQLException
    {
        LOG.info("Begin to init H2 database.");

        try
        {
            Class.forName("org.h2.Driver");
        } catch (Exception e)
        {
            LOG.error("h2 database driver not found. Please ensure the driver jar is in classpath.");
            throw new SQLException("h2 database driver not found");
        }

        Statement st = null;
        try
        {
            st = getStatement();
            Connection conn = st.getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, null, new String[]
            { "TABLE" });
            ArrayList<String> tableList = new ArrayList<String>();
            while (rs.next())
            {
                tableList.add(rs.getString("TABLE_NAME"));
            }
            LOG.debug("Exist tables - " + tableList.size() + " - " + tableList.toString());

            if (!tableList.contains("USER"))
            {
                LOG.info("Create table - user");
                st.execute(User.getSCRIPTS_CREATE_TABLE());
            }
            if (!tableList.contains("FRIENDSHIP"))
            {
                LOG.info("Create table - friendship");
                st.execute(Friendship.getSCRIPTS_CREATE_TABLE());
            }

        } catch (SQLException e)
        {
            LOG.error("Init error", e);
            e.printStackTrace();
            throw new SQLException("Init error");

        } finally
        {
            if (null == st)
                return;

            try
            {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static BoneCP connectionPool;

    private BoneCP getConnectionPool() throws SQLException
    {
        if (null == connectionPool)
        {
            LOG.info("Creating connection pool");
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl("jdbc:h2:file:" + DB_PATH_FILE);
            config.setUsername(DB_USERNAME);
            config.setPassword(DB_PASSWORD);

            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(20);
            config.setPartitionCount(1);

            connectionPool = new BoneCP(config);

            LOG.info("Succeed to create connection pool.");
        }

        return connectionPool;
    }

    public Statement getStatement() throws SQLException
    {
        Connection connection = getConnectionPool().getConnection();
        if (connection == null)
        {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        connection.setAutoCommit(true);
        return connection.createStatement();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException
    {
        Connection connection = getConnectionPool().getConnection();
        if (connection == null)
        {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        connection.setAutoCommit(true);
        return connection.prepareStatement(sql);
    }

}
