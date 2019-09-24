import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class LoginForm {
    private JPanel LoginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton cancelButton;

    private static JFrame frame = null;

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

    public static void main(String[] args) {
//        Message msg = new Message(Message.OKMessage, System.getProperty("java.class.path"));
        frame = new JFrame("Dragon's Lair Login");
        frame.setContentPane(new LoginForm().LoginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
