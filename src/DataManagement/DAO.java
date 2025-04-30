package DataManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;

public abstract class DAO<T> {
    
    private static DataSource ds;
    
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/Tugurio");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
    
    protected Connection connection;
    
    public DAO() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = ds.getConnection();
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public abstract void doSave(T item) throws SQLException;
    public abstract boolean doDelete(int id) throws SQLException;
    public abstract void doUpdate(T item) throws SQLException;
    public abstract T doRetrieveByKey(int id) throws SQLException;
    public abstract ArrayList<T> doRetrieveAll(String ordine) throws SQLException;
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log silently - we're closing anyway
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}