import javax.swing.*;
import java.awt.event.*;

public class PrintReports {
    private JPanel contentPanel;
    private JButton singleComic;
    private JButton singleCustomer;
    private JButton newReleases;
    private JButton allTitles;
    private JButton doneButton;

    private static JFrame frame = null;

    public PrintReports() {
        singleComic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleComic();
            }
        });
        singleCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleCustomer();
            }
        });
        newReleases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newReleases();
            }
        });
        allTitles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allTitles();
            }
        });
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        });

        contentPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) { Done(); }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) { Open(); }
        });
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Print Reports");
            frame.setContentPane(new PrintReports().contentPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    /**
     * Dispose of the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    /**
     * Makes the frame invisible, also known as closing it.
     */
    private void Done() { /*frame.setVisible(false);*/ frame.dispose();}

    /**
     * Opens the window.
     */
    private void Open() {
        frame.setVisible(true);
    }

    private void singleComic() {

    }

    private void singleCustomer() {

    }

    private void newReleases() {

    }

    private void allTitles() {

    }
}
