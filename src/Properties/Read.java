package Properties;

import javax.swing.filechooser.FileSystemView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Read {
    String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    String dbPropPath = rootPath + "/.db.properties";
    static Properties prop = new Properties();

    public Read() {}

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

    public static String getDir() {
        return prop.getProperty("db.url");
    }

    public static String getPW() {
        return prop.getProperty("db.password");
    }

    public static String getUser() {
        return prop.getProperty("db.user");
    }

    public String getRootPath() {
        return rootPath;
    }
}
