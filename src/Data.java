public class Data {
    private static String store = "";
    private static String user = "";
    private static DB db = new DB();

    public static DB DB() { return db; }

    public static String Store() {       return store; }
    public static void Store(String s) { store = s;    }

    public static String User() {        return user; }
    public static void User(String u) {  user = u;    }
}
