import com.mysql.cj.x.protobuf.MysqlxResultset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;

/**
 * This form is responsible for the window of selecting a CSV file to import into the database.
 */
public class CSVForm {
    private JPanel CSVPanel;
    private CSV csvReader = new CSV();

    private static JFrame frame;
    public CSVForm csvForm;
    private JFileChooser jfc;

    /**
     * Frame method
     * @return Returns the frame corresponding to the CSVForm.
     */
    public static JFrame Frame() { return frame; }

    /**
     * Dispose closes the current window.
     */
    public static void Dispose() {
        if (frame != null) frame.dispose();
    }

    /**
     * Generates the CSVForm window.
     */
    public static void Display() {
        new CSVForm();
    }

    /**
     * Constructor for CSVForm. Sets key bindings for shortcuts, sets filters, and calls CSV.java once the file is
     * selected in order to add it to the database.
     */
    public CSVForm() {
        csvForm = this;
        CSVPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a file");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".CSV",  "csv");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getSelectedFile().getPath());
            System.out.println(jfc.getSelectedFile().getName());
            //csvReader.setFileLocation(jfc.getSelectedFile().getPath());
            //csvReader.setName(jfc.getSelectedFile().getPath());
            csvReader.openFile(jfc.getSelectedFile().getPath());
        }

    }

    /**
     * Makes the frame invisible, also known as closing it.
     */
    private void Done() { frame.setVisible(false); }

}
