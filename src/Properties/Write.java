package Properties;

import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Write is responsible for generating the hidden properties file if one does not exist.
 */
public class Write {
    String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    String dbPropPath = rootPath + "/.db.properties";

    /**
     * Constructor. Does not generate any instance-specific information
     */
    public Write() {}

    /**
     * Makes hidden properties file, .db.properties, located in the directory rootPath.
     */
    public void generateFile() {
        try (OutputStream output = new FileOutputStream(dbPropPath)) {
            Properties dbProp = new Properties();

            dbProp.setProperty("db.url", "jdbc:mysql://localhost:3306?serverTimezone=US/Central");
            dbProp.setProperty("db.user", "username");
            dbProp.setProperty("db.password", "password");

            dbProp.store(output, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
