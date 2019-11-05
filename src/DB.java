//import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

//import com.sun.istack.internal.NotNull;

public class DB {

    private class Connection {
        private java.sql.Connection connection = null;

        Connection(String host) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","Aerospace1907");
                connection = DriverManager.getConnection(host + "?serverTimezone=US/Central", "root", "Aerospace1907");
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

        void createCustomerTable() {
            ExecuteStatement("create table customer(id integer not null auto_increment, " +
                    "name varchar(40) not null, " +
                    "phone varchar(20), " +
                    "email varchar(100), " +
                    "primary key(id))");
        }

        void createItemTable() {
            ExecuteStatement("use admin");
            ExecuteStatement("create table item(id integer not null auto_increment, " +
                    "item varchar(300) not null, " +
                    "diamond varchar(100), " +
                    "primary key(id))");
        }

        void createPullListTable() {
            ExecuteStatement("create table pull_list(id integer not null auto_increment, " +
                    "customer_id integer, " +
                    "item_id integer, " +
                    "issue integer, " +
                    "allVariants boolean, " +
                    "quantity integer, " +
                    "isGraphic boolean, " +
                    "primary key(id), " +
                    "foreign key (customer_id) references customer(id), "+
                    "foreign key (item_id) references item(id))");
        }

        void createItemMapTable(){
            ExecuteStatement("create table ItemMap(id integer not null auto_increment, " +
                    "wants integer, " +
                    "got integer, " +
                    "primary key(id), " +
                    "foreign key(wants) references item(id), " +
                    "foreign key(got) references item(id))");
        }

        void createPullItemTable(){
            ExecuteStatement("create table pullItem(id integer not null auto_increment, " +
                    "item_id integer, " +
                    "pull_id integer, " +
                    "primary key(id), " +
                    "foreign key(item_id) references item(id), " +
                    "foreign key(pull_id) references pull_list(id))");
        }

        void createPullDateTable(){
            ExecuteStatement("create table pullDate(id integer not null auto_increment, " +
                    "pull_id integer, " +
                    "date date, " +
                    "primary key(id), " +
                    "foreign key (pull_id) references pull_list(id))");
        }

        void createQuantityTable(){
            ExecuteStatement("create table quantity(id integer not null auto_increment, " +
                    "customer_id integer, " +
                    "pull_id integer, " +
                    "quantity integer, " +
                    "primary key(id), " +
                    "foreign key(customer_id) references customer(id), " +
                    "foreign key(pull_id) references pull_list(id))");
        }



        void InitializeStore(String store) {
            ExecuteStatement("create database " + store);
            ExecuteStatement("use " + store);
            createCustomerTable();
            createItemTable();
            createPullListTable();
            createItemMapTable();
            createPullDateTable();
            createPullItemTable();
            createQuantityTable();
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

    public boolean Login(String username, char[] password) {
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
        db.InitializeStore(store);
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

    public Vector<String> GetCustomers() {
        Vector<String> customers = new Vector<>();
        db.ExecuteStatement("use " + Data.Store());
        ResultSet data = db.ExecutePrepared("select name from customers");

        try {
            if (data != null) {
                while (data.next()) {
                    customers.add(data.getString("name"));
                }
            }
        }
        catch(Exception e) { System.out.println(e); }

        return customers;
    }

    public boolean CustomerExists(String store, String customer) {
        db.ExecuteStatement("use " + store);
        ResultSet data = db.ExecutePrepared("select id " +
                "from customer " +
                "where name = ?", customer);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    public void AddCustomer(String store, String name, String email, String phone) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("insert into customer (name, email, phone) values(?, ?, ?)",
                name, email, phone);
    }

    public String GetCustomerEMail(String store, String name) {
        db.ExecuteStatement("use " + store);
        ResultSet r = db.ExecutePrepared("select customer.email from customer " +
                "where customer.name = ? ", name);
        if (r == null) return "";
        try {
            if (!r.next()) return "";
            return r.getString("customer.email");
        }
        catch (Exception e) { System.out.println(e); }
        return "";
    }

    public String GetCustomerPhone(String store, String name) {
        db.ExecuteStatement("use " + store);
        ResultSet r = db.ExecutePrepared("select customer.phone from ?.customer " +
                "where customer.name = ? ", name);
        if (r == null) return "";
        try {
            if (!r.next()) return "";
            return r.getString("customer.phone");
        }
        catch (Exception e) { System.out.println(e); }
        return "";
    }

    public void UpdateCustomer(String store, String origCustomer, String customer, String email, String phone) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("update customer set name=?, email=?, phone=? where name=?",
                customer, email, phone, origCustomer);
    }

    public void DeleteCustomer(String store, String origCustomer) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("delete from customer where name=?",
                origCustomer);
    }


    void insertItemTable(String itemName, String diamondCode){
        if(!itemExists(diamondCode)) {
            db.ExecuteData("insert into item(item,diamond) values(?,?))", itemName, diamondCode);
        }

    }

    Boolean itemExists(String diamondCode){
        ResultSet theDiamond = db.ExecutePrepared("select diamond from item where diamond = ?",diamondCode);
        try { return theDiamond != null && theDiamond.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;

    }
    void deleteItemTable(String itemName){
        db.ExecuteData("delete from item where item = ?",itemName);

    }
}
