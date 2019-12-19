import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Window used when trying to add or edit a comic.
 */
public class AddOrEditComic extends JDialog {
    private JPanel contentPane;
    private JTextField titleTextField;
    private JTextField diamondTextField;
    private JCheckBox graphicalNovelCheckBox;
    private JCheckBox nonBookCheckBox;
    private JTextField issueTextField;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox<String> idComboBox;

    private static boolean finished;
    private static String comicTitle;
    private static String diamondCode;
    private static String graphicNovel;
    private static String nonBook;
    private static String issue;
    private static String csvId;

    /**
     * Checking to see if they are finished.
     * @return True if finished, false otherwise.
     */
    public static boolean Finished() { return finished; }

    /**
     * Get comic title.
     * @return Comic title.
     */
    public static String ComicTitle() { return comicTitle; }

    /**
     * Get diamond code.
     * @return Diamond code.
     */
    public static String DiamondCode() { return diamondCode; }

    /**
     * Get graphic novel status.
     * @return graphic novel status.
     */
    public static String GraphicNovel() { return graphicNovel; }

    /**
     * Get non book status.
     * @return non book status.
     */
    public static String NonBook() { return nonBook; }

    /**
     * Get issue number.
     * @return Issue number.
     */
    public static String Issue() { return issue; }

    /**
     * Get csv id.
     * @return CSV ID
     */
    public static String CsvId() { return csvId; }

    /**
     * Sets up the window with the proper information prepopulated.
     * @param title Title of comic.
     * @param dCode Diamond code of comic.
     * @param gNovel Graphic novel status.
     * @param nBook Non book status.
     * @param iss Issue number.
     * @param date Date to add to.
     */
    public AddOrEditComic(String title, String dCode, String gNovel, String nBook, String iss, String date){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveButton);

        titleTextField.setText(title);
        diamondTextField.setText(dCode);
        issueTextField.setText(iss);

        if(gNovel != null && !gNovel.isEmpty()){
            boolean gNovelIsSelected = gNovel.equals("1");
            graphicalNovelCheckBox.setSelected(gNovelIsSelected);
        }

        if(nBook != null && !nBook.isEmpty()){
            boolean nBookIsSelected = nBook.equals("1");
            nonBookCheckBox.setSelected(nBookIsSelected);
        }

        idComboBox.setSelectedItem(date);

        Vector<String> dates = Data.DB().getCsvDates();
        idComboBox.setModel(new DefaultComboBoxModel(dates));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cancel();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Save();
            }
        });

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * Saves all the information in the window. Finished set to true.
     */
    private void Save() {
        comicTitle = titleTextField.getText();
        diamondCode = diamondTextField.getText();
        int gn = graphicalNovelCheckBox.isSelected() ? 1 : 0;
        graphicNovel = String.valueOf(gn);
        int nb = nonBookCheckBox.isSelected() ? 1 : 0;
        nonBook = String.valueOf(nb);
        issue = issueTextField.getText();
        if(issue.equals("")){
            issue = null;
        }
        csvId = Data.DB().getCsvDateId(Data.Store(), (String) idComboBox.getSelectedItem());

        if(comicTitle.isEmpty() || diamondCode.isEmpty() || graphicNovel.isEmpty() || nonBook.isEmpty() || csvId.isEmpty()){
            return;
        }

        finished = true;
        dispose();
    }

    /**
     * Set finished to false, get rid of window.
     */
    private void Cancel() {
        finished = false;
        dispose();
    }
}
