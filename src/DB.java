import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

public class DB {

    private class Connection {
        private java.sql.Connection connection = null;

        Connection(String host) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(host + "?serverTimezone=US/Central", "root", "v4d3r@Laptop");
                if (!DBExists("admin")) InitializeDB();
                if (!TableExists("admin", "store")) InitializeStores();
            } catch (Exception e) { System.out.println(e); }
        }

        Statement stmt = null;

        boolean ExecuteStatement(String sql) {
            try {
                if (stmt != null) stmt.close();
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            }
            catch (Exception e) { System.out.println(e); }
            return false;
        }

        private void InitializeDB() {
            ExecuteStatement("create database admin");
            ExecuteStatement("use admin");
            ExecuteStatement("create table user (id integer not null auto_increment, " +
                                                     "name varchar(40) not null, " +
                                                     "password char(40) not null, " +
                                                     "store integer, " +
                                                     "primary key(id))");
            ExecuteStatement("insert into user (name, password) " +
                                              "values ('admin', sha('admin'))");
            InitializeStores();
        }

        void InitializeStores() {
            ExecuteStatement("use admin");
            ExecuteStatement("create table admin.store (id integer not null auto_increment, " +
                                                            "name varchar(128), " +
                                                            "primary key(id))");
        }

        void InitializeStore(String store) {
            ExecutePrepared("create database ?", store);
            ExecutePrepared("use ?", store);
            // create customer table
            // create item table
            // create customer_item table
            // create pull_list table
        }

        PreparedStatement prep = null;

        ResultSet ExecutePrepared(String sql, String...p) {
            try {
                if (prep != null) prep.close();
                prep = connection.prepareStatement(sql);
                int i = 1;
                for (String x: p) prep.setString(i++, x);
                return prep.executeQuery();
            }
            catch (Exception e) { System.out.println(e); }
            return null;
        }

        boolean ExecuteData(String sql, String...p) {
            try {
                if (prep != null) prep.close();
                prep = connection.prepareStatement(sql);
                int i = 1;
                for (String x: p) prep.setString(i++, x);
                return prep.execute();
            }
            catch (Exception e) { System.out.println(e); }
            return false;
        }

        private boolean DBExists(String name) {
            try {
                ResultSet resultSet = connection.getMetaData().getCatalogs();
                while (resultSet.next()) {
                    String databaseName = resultSet.getString(1);
                    if (databaseName.equals(name)) return true;
                }
                resultSet.close();
            }
            catch (Exception e) { System.out.println(e); }
            return false;
        }

        private boolean TableExists(String db, String name) {
            String sql = "SELECT * " +
                         "FROM information_schema.TABLES " +
                         "WHERE (TABLE_SCHEMA = ?) AND (TABLE_NAME = ?)";
            ResultSet r = ExecutePrepared(sql, db, name);
            try { return r != null && r.next(); }
            catch (Exception e) { System.out.println(e); }
            return false;
        }

        boolean Login(String username, String password) {
            ExecuteStatement("use admin");
            ResultSet r = ExecutePrepared("select store from user where name=? and password=sha(?)", username, password);
            try { return r != null && r.next(); }
            catch(Exception e) { System.out.println(e); }
            return false;
        }
    }

    private Connection db = null;

    private void Init(String host) {
        db = new Connection(host);
    }

    public DB(String host) { Init(host); }
    public DB() {
        String defaultHost = "jdbc:mysql://localhost:3306";
        Init(defaultHost);
    }

    public boolean Login(String username, @NotNull char[] password) {
        String pass = "";
        for (Character c: password) pass += c;
        return db.Login(username, pass);
    }

    public Vector<String> GetStores() {
        Vector<String> stores = new Vector<>();
        ResultSet data = db.ExecutePrepared("select name from admin.store");

        try {
            if (data != null) {
                while (data.next()) {
                    stores.add(data.getString("name"));
                }
            }
        }
        catch(Exception e) { System.out.println(e); }

        return stores;
    }

    public boolean StoreInUse(String store) {
        ResultSet data = db.ExecutePrepared("select admin.store.id " +
                                                 "from admin.store, admin.user " +
                                                 "where admin.store.name = ? and admin.user.store = admin.store.id", store);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    public boolean StoreExists(String store) {
        ResultSet data = db.ExecutePrepared("select id " +
                                                 "from admin.store " +
                                                 "where name = ?", store);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    public void AddStore(String store) {
        db.ExecuteData("insert into admin.store (name) values(?)", store);
    }

    public void DeleteStore(String store) {
        db.ExecuteData("delete from admin.store where admin.store.name = ?", store);
    }

    public void RenameStore(String orig, String name) {
        db.ExecuteData("update admin.store set admin.store.name = ? where admin.store.name = ?", name, orig);
    }

    public int GetStoreId(String store) {
        ResultSet data = db.ExecutePrepared("select id from admin.store where admin.store.name = ?", store);
        try {
            if (data != null) {
                while (data.next()) {
                    return data.getInt("id");
                }
            }
        }
        catch (Exception e) { System.out.println(e); }
        return -1;
    }

    public Vector<String> GetUsers() {
        Vector<String> users = new Vector<>();
        ResultSet data = db.ExecutePrepared("select name from admin.user");

        try {
            if (data != null) {
                while (data.next()) {
                    users.add(data.getString("name"));
                }
            }
        }
        catch(Exception e) { System.out.println(e); }

        return users;
    }

    public boolean UserExists(String user) {
        ResultSet data = db.ExecutePrepared("select id " +
                "from admin.user " +
                "where name = ?", user);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    public void AddUser(String user, String password, String store) {
        int id = GetStoreId(store);
        db.ExecuteData("insert into admin.user (name, password, store) values(?, sha(?), ?)",
                       user, password, Integer.toString(id));
    }

    public void DeleteUser(String user) {
        db.ExecuteData("delete from admin.user where admin.user.name = ?", user);
    }

    public void UpdateUser(String orig, String name, String password, String store) {
        int id = GetStoreId(store);
        if (password.isEmpty()) db.ExecuteData("update admin.user set admin.user.name = ?, admin.user.store = ? " +
                "where admin.user.name = ?", name, Integer.toString(id), orig);
        else db.ExecuteData("update admin.user set admin.user.name = ?, admin.user.password = sha(?), admin.user.store = ? " +
                                              "where admin.user.name = ?", name, password, Integer.toString(id), orig);
    }

    public String GetUserStore(String user) {
        ResultSet r = db.ExecutePrepared("select store.name from admin.user, admin.store " +
                                                                "where admin.user.name = ? " +
                                                                  "and admin.user.store = admin.store.id", user);
        if (r == null) return "";
        try {
            if (!r.next()) return "";
            return r.getString("store.name");
        }
        catch (Exception e) { System.out.println(e); }
        return "";
    }
}