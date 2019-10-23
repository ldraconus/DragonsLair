import com.mysql.cj.x.protobuf.MysqlxResultset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;

public class CSVForm {
    private JPanel CSVPanel;

    private static JFrame frame;
    public CSVForm csvForm;
    private JFileChooser jfc;

    public static JFrame Frame() { return frame; }
    public static void Dispose() { // dispose sub window
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        new CSVForm();
    }

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
        }

    }

    private void Done() { frame.setVisible(false); }

}
