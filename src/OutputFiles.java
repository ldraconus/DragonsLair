import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OutputFiles {
    private JPanel SavePanel;
    private JFileChooser SaveLocation;
    File fileToSave;

    private static JFrame frame = null;

    private void saveWindow(String content) {
        SaveLocation.setDialogTitle("Specify a file to save");
        SaveLocation.setAcceptAllFileFilterUsed(false);
        SaveLocation.addChoosableFileFilter(new FileNameExtensionFilter("txt file", "txt"));
        SaveLocation.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = SaveLocation.showSaveDialog(SavePanel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = SaveLocation.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            writeFile(fileToSave.toString() + ".txt", content);
        }
    }

    private void writeFile(String location, String content) {
        try {
            Files.write(Paths.get(location), content.getBytes());
        } catch (IOException io) {
            System.err.println(io.getMessage());
        }
    }

    public File getFileToSave() {
        return fileToSave;
    }

    /**
     * Create a new JFrame and display the initial UI components.
     */
    public void Display(String content) {
        saveWindow(content);
    }

    public OutputFiles() {
        SavePanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Makes the frame invisible, also known as closing it.
     */
    private void Done() { /*frame.setVisible(false);*/ frame.dispose();}

    /**
     * Dispose of the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }
}
