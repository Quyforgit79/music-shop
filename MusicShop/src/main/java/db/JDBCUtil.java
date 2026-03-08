/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class JDBCUtil {

    private Connection conn;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=MusicShop;encrypt=true;trustServerCertificate=true;useUnicode=true;characterEncoding=UTF-8";
    private static final String DB_USER = "sa";
    private static final String BD_PASSWORD = "12345689quy";

    public JDBCUtil() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, BD_PASSWORD);
            System.out.println("Connect success");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Eror");
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public static void closeConnection(Connection connect) {
        if (connect != null) {
            try {
                if (!connect.isClosed()) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet execSelectQuery(String query, Object[] params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }

        return ps.executeQuery();
    }

    public ResultSet execSelectQuery(String query) throws SQLException {
        return this.execSelectQuery(query, null);
    }

    public int execQuery(String query, Object[] params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }

        return ps.executeUpdate();
    }

    public ResultSet execInsertWithGeneratedKeys(String query, Object[] params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }

        ps.executeUpdate();

        return ps.getGeneratedKeys();
    }

    public int execStoredProcedure(String procedureCall, Object[] params, int outputParamIndex) throws SQLException {
        try ( Connection conn = getConnection();  CallableStatement stmt = conn.prepareCall(procedureCall)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] == null) {
                        stmt.setNull(i + 1, Types.INTEGER);
                    } else {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
            }
            stmt.registerOutParameter(outputParamIndex, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(outputParamIndex);
        }
    }
}
