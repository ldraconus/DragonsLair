import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class AddSearchTerm extends JDialog{
    private JTextField diamondCodeField;
    private JTextField issueField;
    private JTextField displayNameField;
    private JCheckBox graphicCheckBox;
    private JCheckBox nonBookCheckBox;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private JTextField searchField;

    public AddSearchTerm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void onCancel() {
        dispose();
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    /*
    public void Display() {
        if (frame == null) {
            frame = new JFrame("Add Pull Terms");
            frame.setContentPane(contentPane);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }*/

    private void onOk() {
        String displayName = displayNameField.getText();
        String diamondName = diamondCodeField.getText();
        String issueName = issueField.getText();
        String matchTerm = searchField.getText();
        String graphicNovel = graphicCheckBox.isSelected() ? "1" : "0";
        String nonBook = nonBookCheckBox.isSelected() ? "1" : "0";

        if (displayName.compareTo("") == 0) {
            String message = "Please enter a display name";
            JOptionPane.showMessageDialog(this.contentPane, message);
            return;
        }

        if (diamondName.length() == 0) {
            diamondName = null;
        }

        if (issueName.length() == 0) {
            issueName = null;
        }

        /*if (!checkExtra(diamondName, issueName, graphicNovel, nonBook)) {
            JOptionPane.showMessageDialog(null, "Please only have one field filled out.");
            return;
        }*/

        Data.DB().insertSearchTerms(Data.Store(), displayName, diamondName, issueName,graphicNovel, nonBook, matchTerm);
        dispose();
    }

    private boolean checkExtra(String first, String second, String graphic, String other) {
        int nullCount = 0;

        if ((first == null)) {nullCount++;}
        if ((second == null)) {nullCount++;}
        if (graphic.equals("1")) {nullCount++;}
        if (other.equals("1")) {nullCount++;}

        System.out.printf("Null count: %d\n", nullCount);

        if (nullCount == 1) {return true;}
        else {return false;}
    }
}
