package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Виктор on 07.10.2016.
 */
public class MainProcess implements Runnable {
    private Thread thread;
    private Controller controller = null;
    private Socket socket = null;
    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;
    private String adress = "localhost";
    private int mainPort = 1111;
    private String userName = null;


    MainProcess(Controller controller){
        this.controller = controller;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        if (!controller.serverIP.getText().equals("")) adress = controller.serverIP.getText();
        try {
            InetAddress inetAddress = InetAddress.getByName(adress);
            socket = new Socket(inetAddress, mainPort);
            controller.loginButton.setDisable(false);
            controller.connectButton.setDisable(true);
            controller.serverIP.setDisable(true);
            controller.serverPort.setDisable(true);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("con/" + InetAddress.getLocalHost().getHostAddress());
            outputStream.flush();
            int newPort = Integer.parseInt(inputStream.readUTF());
            new ClientProcess(newPort, controller);
        }catch (Exception e){}
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
