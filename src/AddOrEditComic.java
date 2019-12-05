import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

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

    public static boolean Finished() { return finished; }

    public static String ComicTitle() { return comicTitle; }

    public static String DiamondCode() { return diamondCode; }

    public static String GraphicNovel() { return graphicNovel; }

    public static String NonBook() { return nonBook; }

    public static String Issue() { return issue; }

    public static String CsvId() { return csvId; }

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

    private void Cancel() {
        finished = false;
        dispose();
    }
}
