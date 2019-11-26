package Properties;

import javax.swing.filechooser.FileSystemView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read is responsible for reading the properties file for the database connection.
 */
public class Read {
    String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    String dbPropPath = rootPath + "/.db.properties";
    static Properties prop = new Properties();

    /**
     * Constructor. Nothing specific to the instance is created.
     */
    public Read() {}

    /**
     * Attempts to read the properties file found at rootPath.
     * @return true if it was able to read connection information, false otherwise or if username and password have not
     * been changed from the default values.
     */
    public boolean readFile () {
        try (InputStream input = new FileInputStream(dbPropPath)) {

            prop.load(input);

            if (getUser().compareTo("username") == 0 && getPW().compareTo("password") == 0) {
                return false;
            }

            return true;

        } catch (FileNotFoundException fnf) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get directory contained in the properties file.
     * @return Database connection location.
     */
    public static String getDir() {
        return prop.getProperty("db.url");
    }

    /**
     * Get the password contained in the properties file.
     * @return Database password.
     */
    public static String getPW() {
        return prop.getProperty("db.password");
    }

    /**
     * Get the user name contained in the properties file.
     * @return Database username.
     */
    public static String getUser() {
        return prop.getProperty("db.user");
    }

    /**
     * Get path that the hidden .db.properties file is contained in.
     * @return Path to the hidden properties file.
     */
    public String getRootPath() {
        return rootPath;
    }
}
