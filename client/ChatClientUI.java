import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChatClientUI extends JFrame {
    private JTextArea chatArea;
    private JTextArea userList;
    private JTextField inputField;
    private JButton sendButton;
    private JButton fileButton;
    private Main socketConnection;
    private JFileChooser fileChooser;
    private String chat;

    public ChatClientUI() {
        this.chat = "";
        this.socketConnection = new Main("localhost");
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();

        setVisible(true);
    }

    private void initComponents() {
        chatArea = new JTextArea();
        
        chatArea.setEditable(false);
        chatArea.setText(chat);
        socketConnection.setChat(chatArea);
        chatArea.setVisible(true);

        userList = new JTextArea();
        userList.setEditable(false);
        userList.setVisible(true);
        userList.setBackground(getForeground());
        socketConnection.setUserList(userList);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // socketConnection.send(inputField.getText());
            }
        });
        sendButton = new JButton("Senden");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socketConnection.send(inputField.getText());
                inputField.setText("");
            }
        });
        String icon = "";
        byte[] bIcon = {(byte) 0x1F, (byte) 0xC4};
        try {
            icon = new String(bIcon, "UTF-8");
        } catch (Exception e) {
            System.out.println("Encoding Exception occured " + e);
        }
        
        fileChooser = new JFileChooser();
        fileButton = new JButton(icon);
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(inputField);
                System.out.println(returnVal);
                File f = fileChooser.getSelectedFile();
                inputField.setText(f.toString());
                socketConnection.sendPic(f.toString());
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(new JLabel("Chat"), BorderLayout.NORTH);
        JScrollPane scrollChat = new JScrollPane();
        scrollChat.setViewportView(chatArea);
        scrollChat.setVerticalScrollBar(scrollChat.createVerticalScrollBar());
        chatPanel.add(scrollChat, BorderLayout.CENTER);
        JPanel userInputs = new JPanel(new BorderLayout());
        userInputs.add(inputField, BorderLayout.CENTER);
        userInputs.add(sendButton, BorderLayout.EAST);
        userInputs.add(fileButton, BorderLayout.WEST);
        chatPanel.add(userInputs, BorderLayout.SOUTH);

        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.add(new JLabel("User List"), BorderLayout.NORTH);
        JScrollPane scrollUsers = new JScrollPane();
        scrollUsers.setViewportView(userList);
        scrollUsers.setVerticalScrollBar(scrollUsers.createVerticalScrollBar());
        userListPanel.add(scrollUsers, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatPanel, userListPanel);
        splitPane.setResizeWeight(0.7);

        add(splitPane, BorderLayout.CENTER);

        JButton loginButton = new JButton("Anmelden/Registrieren");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginWindow();
            }
        });
        add(loginButton, BorderLayout.NORTH);
    }

    private void openLoginWindow() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Benutzername:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        JPasswordField passwordField = new JPasswordField();

        JLabel invisLabel = new JLabel(""); // dient zum verschieben vom login button nach rechts, kann bestimmt schöner programmiert werden
        JButton loginButton = new JButton("Anmelden");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tba
                socketConnection.setUserName(usernameField.getText());
                socketConnection.setPwd(new String(passwordField.getPassword()));
                socketConnection.start();
                loginFrame.dispose();
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(invisLabel);
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClientUI();
            }
        });
    }
}
