package Properties;

import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Write {
    String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    String dbPropPath = rootPath + "/.db.properties";

    public Write() {}

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
