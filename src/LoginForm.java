import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import Properties.*;

public class LoginForm {
    private JPanel LoginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton cancelButton;

    private static JFrame frame = null;

    public static JFrame Frame() { return frame; }
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        frame = new JFrame("Dragon's Lair Login");
        frame.setContentPane(new LoginForm().LoginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean Login() { return Data.DB().Login(usernameField.getText(), passwordField.getPassword()); }

    private void DoLogin() {
        if (!Login()) {
            frame.setVisible(false);
            Message msg = new Message(Message.OKMessage, "Username or Password is incorrect");
            frame.setVisible(true);
        }
        else {
            frame.setVisible(false);
            Data.User(usernameField.getText());
            Data.Store(Data.DB().GetUserStore(Data.User()));
            MainForm.Display();
        }
    }

    private LoginForm() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DoLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        LoginPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        LoginPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DoLogin();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static boolean CloseYesNo() {
        Message msg = new Message(Message.YesNoMessage, "Do you really want to close the application?");
        return msg.getButton() == msg.YesButton;
    }

    public static void main(String[] args) {
        Write prefwrite = new Write();
        Read prefread = new Read();
        prefread.readFile();
        if (prefread.readFile()) {
            System.out.println("DB Location: " + prefread.getDir() + " DB Username: " + prefread.getUser() + " DB Password: " + prefread.getPW());
            Display();
        }
        else {
            prefwrite.generateFile();
            System.out.println("db.properties file created in default directory.");
            String message = "Please configure .db.properties file in ";
            message += prefread.getRootPath();
            message += "\nUsername cannot be 'username' and password cannot be 'password'.";
            JOptionPane.showMessageDialog(null, message);
        }
    }
}
