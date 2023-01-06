package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;

public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("Mysqlewq1123!@");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        config.setMaximumPoolSize(50);
        System.out.println("max-pool-size:" + config.getMaximumPoolSize());
        ;
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void fetchData() throws SQLException {
        String SQL_QUERY = "select * from user";
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataSource.getConnection();
            pst = con.prepareStatement(SQL_QUERY);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("iduser"));
                Thread.sleep(1000l);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(con != null) con.close();
            if(pst != null) pst.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        for (int i = 0; i < 5000; i++) {
            System.out.print("i:" + i);
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        fetchData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}