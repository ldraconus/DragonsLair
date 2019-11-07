import Properties.Read;

/**
 * Main class for holding information pertaining to the database.
 */
public class Data {
    private static String store = "";
    private static String user = "";
    private static DB db = new DB(Read.getDir(), Read.getUser(), Read.getPW());

    /**
     * Database get function.
     * @return Database corresponding to this instance.
     */
    public static DB DB() { return db; }

    /**
     * Get store method.
     * @return Returns the store name.
     */
    public static String Store() {       return store; }

    /**
     * Set store method.
     * @param s Name to set for the store.
     */
    public static void Store(String s) { store = s;    }

    /**
     * Get user method.
     * @return Returns the user name.
     */
    public static String User() {        return user; }

    /**
     * Set user name.
     * @param u Name to set for the user.
     */
    public static void User(String u) {  user = u;    }
}
