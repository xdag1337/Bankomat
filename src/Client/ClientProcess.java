package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Виктор on 07.10.2016.
 */
public class ClientProcess implements Runnable {
    int port = 0;
    Controller controller = null;
    Thread thread;
    DataInputStream inputStream;
    static DataOutputStream outputStream;

    ClientProcess(int port, Controller controller){
        this.port = port;
        this.controller = controller;
        this.thread = new Thread(this);
        this.thread.start();
    }
    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address, port);
            controller.updateLog("Connected to port " + port);
            controller.loginButton.setDisable(false);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true){
                String[] s = inputStream.readUTF().split("/");
                switch (s[0]){
                    case "auth": if (s[1].equals("ok")) {
                        controller.updateLog("Login ok");
                        controller.userName.setDisable(true);
                        controller.userPassword.setDisable(true);
                        controller.chekcRem.setDisable(true);
                        controller.userLayout.setVisible(true);
                        controller.loginButton.setDisable(true);
                        controller.logoutButton.setDisable(false);
                        sendQuery("bal/" + controller.userName.getText() + "/" + controller.userPassword.getText());
                    }else {
                        if (s[2].equals("usNF")) controller.updateLog("User not found");
                        if (s[2].equals("usLN")) controller.updateLog("User login now");
                    } break;

                    case "bal":
                        controller.labelName.setText(controller.userName.getText());
                        controller.labelBalance.setText(s[1]); break;

                    case "tra": if (s[1].equals("ok")){
                            controller.updateLog("Transaction complite");
                            controller.transactionLayout.setVisible(false);
                            sendQuery("bal/" + controller.userName.getText() + "/" + controller.userPassword.getText());
                        }else controller.updateLog("Transaction error");
                        break;
                    default: break;
                }
            }
        }catch (Exception e){}
    }
    static void sendQuery (String q) throws IOException {
        outputStream.writeUTF(q);
        outputStream.flush();
    }
}
