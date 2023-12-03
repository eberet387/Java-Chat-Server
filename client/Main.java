import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

public class Main extends Thread {

    private String serverName;
    private String userName;
    private String pwd;
    private Socket socket;
    private JTextArea chat;

    public Main(String serverName) {
        this.serverName = serverName;
    }

    public void run() {
        
        try {
            this.socket = new Socket(serverName, 1234);
            //reading the input from server

            
            // use DataOutputStream instead of PrintWriter
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String requestUsername = input.readUTF();
            if(requestUsername.equals("Nutzernamen eingeben:")) {
                out.writeUTF(this.userName);
            }

            String requestPwd = input.readUTF();
            if(requestPwd.equals("Log dich mit deinem Passwort ein:")) {
                out.writeUTF(this.pwd);
            }
           
           while(!socket.isClosed()) {
            if (input.available() != 0) {
                    String response = input.readUTF();
                    chat.setText(chat.getText() + response + "\n");
                    System.out.println(response);
                if (response.equals("exit")) {
                    out.writeUTF(response);
                    // scanner.close();
                    socket.close();
                }
            }
            sleep(50);
        }

           // scanner.close();
            
        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        } 
    }

    public void send(String msg) {
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);
        } catch (IOException e) {
            System.out.println("IOException occurd in Main" + e);
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setChat(JTextArea chat) {
        this.chat = chat;
    }
}