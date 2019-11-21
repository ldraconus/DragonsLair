//import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

//import com.sun.istack.internal.NotNull;

/**
 * This class initializes and manages the database used to store all the information
 * in Dragon's Lair. This information is used to print the pull list.
 */
public class DB {

    /**
     * Used for connecting to the database.
     */
    private class Connection {
        private java.sql.Connection connection = null;

        Connection(String host, String name, String pass) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(host, name, pass);
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

        /**
         * Used to initialize the database. Creates the user and store tables.
         */
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

        /**
         * Used to hold all the names of the current store locations of Dragon's Lair, currently there are two.
         */
        void InitializeStores() {
            ExecuteStatement("use admin");
            ExecuteStatement("create table admin.store (id integer not null auto_increment, " +
                                                            "name varchar(128), " +
                                                            "primary key(id))");
        }

        /**
         * Holds all the required information about customers to contact them on a future date.
         */
        void createCustomerTable() {
        	ExecuteStatement("create table customer(id integer not null auto_increment, " +
        												"name varchar(40) not null, " +
        												"phone varchar(20), " + 
        												"email varchar(100), " + 
        												"primary key(id))");
        }

        /**
         * Customers use different terms when defining what they want compared to what the
         * store actually has.
         */
        void createSearchTerms() {
        	ExecuteStatement("create table searchTerms(id integer not null auto_increment, " +
        											"name varchar(128) not null, " +
                                                    "diamond varchar(100), " +
                                                    "issue integer, " +
                                                    "graphicNovel integer, " +
                                                    "collection integer, " +
                                                    "nonBook integer, " +
                                                    "matches varchar(128), " +
        											"primary key(id))");
        }

        /**
         * This table matches a search term with a customer and how many comics using that search
         * term they want.
         */
        void createPullList() {
            ExecuteStatement("create table pull_list(id integer not null auto_increment, " +
                                                    "customer_id integer, " +
                                                    "searchTerm_id integer, " +
                                                    "number integer, " +
                                                    "primary key(id), " +
                                                    "foreign key (customer_id) references customer(id), "+
                                                    "foreign key (searchTerm_id) references searchTerms(id))");
        }


        /**
         * Some search terms that customers use when describing what items they want are the same so
         * various terms that mean the same thing are matched here.
         */
        void createSynonyms(){
            ExecuteStatement("create table synonyms(id integer not null auto_increment, " +
                                                "matched_id integer, " +
                                                "sameAs_id integer, " +
                                                "primary key(id), " +
                                                "foreign key(matched_id) references searchTerms(id), " +
                                                "foreign key(sameAs_id) references searchTerms(id))");
        }

        /**
         * This stores the list of dates for the csv entries. This will also help determine new releases.
         */
        void createCsvDates(){
            ExecuteStatement("create table csvDates(id integer not null auto_increment, " +
                    "csvDate date, " +
                    "primary key(id))");
        }

        /**
         * This is the actual inventory of Dragon's Lair. All the entries from a csv file are parsed and placed here.
         */
        void createCsvEntries(){
            ExecuteStatement("create table csvEntries(id integer not null auto_increment, " +
                                                "title varchar(128), " +
                                                "issue integer, " +
                                                "graphicNovel integer, " +
                                                "collection integer, " +
                                                "nonBook integer, " +
                                                "diamond varchar(20), " +
                                                "csv_id integer, " +
                                                "primary key(id), " +
                                                "foreign key (csv_id) references csvDates(id), " +
                                                "index a(diamond))");
        }

        /**
         * Matches a customer with an item in inventory (csvEntries item) and how many they want.
         */
        void createPulls(){
            ExecuteStatement("create table pulls(id integer not null auto_increment, " +
                                                "customer_id integer, " +
                                                "csvEntries_id integer, " +
                                                "number integer, " +
                                                "primary key(id), " +
                                                "foreign key(customer_id) references customer(id), " +
                                                "foreign key(csvEntries_id) references csvEntries(id))");
        }


        /**
         * Used to initialize a store. Creates all the required tables.
         * @param store: The name of the store being created.
         */
        void InitializeStore(String store) {
            ExecuteStatement("create database " + store);
            ExecuteStatement("use " + store);
            createCustomerTable();
            createCsvDates();
            createSearchTerms();
            createPullList();
            createSynonyms();
            createCsvEntries();
            createPulls();

        }

        PreparedStatement prep = null;

        /**
         * Used to execute a prepared SQL statement.
         * @param sql: The statement being used to access the database.
         * @param p: Additional arguments part of the statement to access the database.
         * @return: Returns the result set, or the tuples of the result of the SQL statement.
         */
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

        /**
         * Returns True if there is data in the database or False if there is none.
         * @param sql: The SQL statement used to access the database.
         * @param p: Additional arguments to the SQL statement.
         * @return: True or False.
         */
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

        /**
         * Determines if the database exists.
         * @param name: The name of the database.
         * @return: True or False.
         */
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

        /**
         * Determines if a particular table exists in the given database.
         * @param db: The name of the database.
         * @param name: The name of the table.
         * @return: True or False.
         */
        private boolean TableExists(String db, String name) {
            String sql = "SELECT * " +
                         "FROM information_schema.TABLES " +
                         "WHERE (TABLE_SCHEMA = ?) AND (TABLE_NAME = ?)";
            ResultSet r = ExecutePrepared(sql, db, name);
            try { return r != null && r.next(); }
            catch (Exception e) { System.out.println(e); }
            return false;
        }

        /**
         * Determines if a given user with a password can login.
         * @param username: The user trying to login.
         * @param password: The password the user is trying to use.
         * @return: True or False.
         */
        boolean Login(String username, String password) {
            ExecuteStatement("use admin");
            ResultSet r = ExecutePrepared("select store from user where name=? and password=sha(?)", username, password);
            try { return r != null && r.next(); }
            catch(Exception e) { System.out.println(e); }
            return false;
        }
    }

    private Connection db = null;

    /**
     * Used to establish a connection with the database.
     * @param host: The host containing the database.
     * @param user: The user trying to connect to the host.
     * @param password: The password for the given user connecting to the host.
     */
    private void Init(String host, String user, String password) {
        db = new Connection(host, user, password);
    }

    public DB(String host, String user, String pass) { Init(host, user, pass); }

    /**
     * Determines if a user can or cannot login.
     * @param username: The user trying to login.
     * @param password: The password of the user.
     * @return: True or False.
     */
    public boolean Login(String username, char[] password) {
        String pass = "";
        for (Character c: password) pass += c;
        return db.Login(username, pass);
    }

    /**
     * Returns all the stores in the database.
     * @return: A vector containing the stores.
     */
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

    /**
     * Returns a boolean value depending if there are more stores.
     * @param store: The name of the store.
     * @return: True or False.
     */
    public boolean StoreInUse(String store) {
        ResultSet data = db.ExecutePrepared("select admin.store.id " +
                                                 "from admin.store, admin.user " +
                                                 "where admin.store.name = ? and admin.user.store = admin.store.id", store);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    /**
     * Determines if a particular exists.
     * @param store: The name of the store.
     * @return: True or False.
     */
    public boolean StoreExists(String store) {
        ResultSet data = db.ExecutePrepared("select id " +
                                                 "from admin.store " +
                                                 "where name = ?", store);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    /**
     * Adds a store to the database.
     * @param store: The name of the store being added.
     */
    public void AddStore(String store) {
        db.ExecuteData("insert into admin.store (name) values(?)", store);
        db.InitializeStore(store);
    }

    /**
     * Removes a store from the database.
     * @param store: The name of the store.
     */
    public void DeleteStore(String store) {
        db.ExecuteData("delete from admin.store where admin.store.name = ?", store);
    }

    /**
     * Renames a given store.
     * @param orig: The original name of the store.
     * @param name: The new name of the store.
     */
    public void RenameStore(String orig, String name) {
        db.ExecuteData("update admin.store set admin.store.name = ? where admin.store.name = ?", name, orig);
    }

    /**
     * Returns the id for a particular store.
     * @param store: The name of store for getting the id.
     * @return
     */
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

    /**
     * Returns the users from the database.
     * @return: A vector of users.
     */
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

    /**
     * Determines if a particular user exists in the database.
     * @param user: The name of the user.
     * @return: True or False.
     */
    public boolean UserExists(String user) {
        ResultSet data = db.ExecutePrepared("select id " +
                "from admin.user " +
                "where name = ?", user);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    /**
     * Adds a user with a password to the given store.
     * @param user: The name of user.
     * @param password: The password for the user.
     * @param store: The store the user is associated with.
     */
    public void AddUser(String user, String password, String store) {
        int id = GetStoreId(store);
        db.ExecuteData("insert into admin.user (name, password, store) values(?, sha(?), ?)",
                       user, password, Integer.toString(id));
    }

    /**
     * Removes a user from the store.
     * @param user: The name of the user.
     */
    public void DeleteUser(String user) {
        db.ExecuteData("delete from admin.user where admin.user.name = ?", user);
    }

    /**
     * Changes the name of a user.
     * @param orig: The original name of the user.
     * @param name: The new name of the user.
     * @param password: The password of the user.
     * @param store: The name of the store the user is associated with.
     */
    public void UpdateUser(String orig, String name, String password, String store) {
        int id = GetStoreId(store);
        if (password.isEmpty()) db.ExecuteData("update admin.user set admin.user.name = ?, admin.user.store = ? " +
                "where admin.user.name = ?", name, Integer.toString(id), orig);
        else db.ExecuteData("update admin.user set admin.user.name = ?, admin.user.password = sha(?), admin.user.store = ? " +
                                              "where admin.user.name = ?", name, password, Integer.toString(id), orig);
    }

    /**
     * Returns the name of the store for a particular user.
     * @param user: The name of the user.
     * @return: The name of the store.
     */
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

    /**
     * Returns all the customers from the database.
     * @return: A vector with all the customers.
     */
    public Vector<String> GetCustomers() {
        Vector<String> customers = new Vector<>();
        db.ExecuteStatement("use " + Data.Store());
        ResultSet data = db.ExecutePrepared("select name from customer");

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

    /**
     * Determines if a customer exists in the given store.
     * @param store: The store the customer visited.
     * @param customer: The name of the customer.
     * @return
     */
    public boolean CustomerExists(String store, String customer) {
        db.ExecuteStatement("use " + store);
        ResultSet data = db.ExecutePrepared("select id " +
                "from customer " +
                "where name = ?", customer);
        try { return data != null && data.next(); }
        catch (Exception e) { System.out.println(e); }
        return false;
    }

    /**
     * Adds a customer to the database.
     * @param store: The store that the customer visited.
     * @param name: The name of the customer.
     * @param email: The email of the customer.
     * @param phone: The phone number of the customer.
     */
    public void AddCustomer(String store, String name, String email, String phone) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("insert into customer (name, email, phone) values(?, ?, ?)",
                name, email, phone);
    }

    /**
     * Returns the customer's email.
     * @param store: The name of the store with the customer.
     * @param name: The name of the customer.
     * @return
     */
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

    /**
     * Returns the customer's phone number.
     * @param store: The store the customer is associated with.
     * @param name: The name of the customer.
     * @return: The customer's phone number.
     */
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

    /**
     * Updates a customer value.
     * @param store: The store the customer is associated with.
     * @param origCustomer: The original name of the customer.
     * @param customer: The new name of the customer.
     * @param email: The new email for the customer.
     * @param phone: The new phone for the customer.
     */
    public void UpdateCustomer(String store, String origCustomer, String customer, String email, String phone) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("update customer set name=?, email=?, phone=? where name=?",
                customer, email, phone, origCustomer);
    }

    /**
     * Deletes a customer from the database.
     * @param store: The store the customer is from.
     * @param origCustomer: The name of the customer being deleted.
     */
    public void DeleteCustomer(String store, String origCustomer) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("delete from customer where name=?",
                origCustomer);
    }

    /**
     * Determines if an item exists.
     * @param diamondCode: The diamond code of the item used to identify it.
     * @return
     */
    Boolean csvEntryExists(String diamondCode, String store) {
        db.ExecuteStatement("use " + store);
        ResultSet theDiamond = db.ExecutePrepared("select diamond from csvEntries where diamond = ?", diamondCode);
        try { return theDiamond != null && theDiamond.next(); }
        catch (Exception e) {System.out.println(e);
        return false;
    }
}

    /**
     *
     * @param title The name of the item.
     * @param diamondCode The identification for an item.
     * @param issue The issue of the comic.
     * @param graphicNovel Determines if it is a graphicNovel.
     * @param collection Determines which collection the item is.
     * @param nonBook Determines if the item is a book or not.
     * @param csvId The date csv file was read in. The most recent date determines new releases.
     * @param store
     */
    public void insertCsvEntries(String title, String diamondCode, String issue, String graphicNovel,
                                 String collection, String nonBook, String csvId, String store) {

        if (!csvEntryExists(diamondCode,store)) {
            db.ExecuteStatement("use " +  store);
            db.ExecuteData("insert into csvEntries(title, issue, graphicNovel, collection, nonBook, diamond, csv_id) " +
                    "values(?,?,?,?,?,?,?)", title, issue, graphicNovel, collection, nonBook, diamondCode,csvId);
        }
        else {
            System.out.println("skipped");
        }
    }

    /**
     * This method should be deleted immediately.
     */
    public void insertItemTable(String itemName, String diamondCode, String store) {

        if (!csvEntryExists(diamondCode,store)) {
            db.ExecuteStatement("use " + store);
            db.ExecuteData("insert into csvEntries(title, diamond) " +
                    "values(?,?)", itemName, diamondCode);
        } else {
            System.out.println("skipped");
        }
    }

    /**
     *
     * @param date The date of a particular csv entry
     * @param store The store being used.
     */
    public void insertCsvDates(String date,String store){
        db.ExecuteStatement("use " + store);
        db.ExecuteData("insert into csvDates(csvDate) " + "values(?)", date);
    }

    /**
     *
     * @param store The store being used.
     * @param date The date of the csv we want.
     * @return
     */
    public String getCsvDateId(String store, String date){
        db.ExecuteStatement("use " + store);
        ResultSet r = db.ExecutePrepared("select csvDates.csvDate from ?.csvDates " +
                "where csvDates.csvDate = ? ", date);
        if (r == null) return "";
        try {
            if (!r.next()) return "";
            return r.getString("csvDates.id");
        }
        catch (Exception e) { System.out.println(e); }
        return "";
    }

    /**
     * Grabs all the names of the csvEntries
     * @return
     */
    public Vector<String> getCsvEntries() {
        Vector<String> csvEntries = new Vector<>();
        db.ExecuteStatement("use " + Data.Store());
        ResultSet data = db.ExecutePrepared("select title from csvEntries");

        try {
            if (data != null) {
                while (data.next()) {
                    csvEntries.add(data.getString("title"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return csvEntries;
    }

    /**
     * Deletes a customer from the database.
     * @param store: The store the customer is from.
     * @param diamond: The diamond code of the csv entry.
     */
    public void deleteCsvEntry(String store, String diamond) {
        db.ExecuteStatement("use " + store);
        db.ExecuteData("delete from csvEntry where diamond=?", diamond);
    }
}
