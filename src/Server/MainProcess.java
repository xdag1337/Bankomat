package Server;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Виктор on 06.10.2016.
 */
public class MainProcess extends Thread {
    private Thread thread = null;
    private Controller controller = null;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private Random random = null;
    private int mainPort = 1111;

    MainProcess(Controller controller){
        this.controller = controller;
        this.random = new Random();
        this.thread = new Thread(this);
        this.thread.start();
    }
    @Override
    public void run() {

        try{
            synchronized (MainProcess.class) {
                serverSocket = new ServerSocket(mainPort);
                if (!controller.serverStatus) updateInterface();
                socket = serverSocket.accept();
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                String[] connect = inputStream.readUTF().split("/");
                if (connect[0].equals("con")) {
                    int newPort = random.nextInt(2000);
                    if (!controller.clietnThreads.containsKey(connect[1])){
                        new ServerProcess(newPort, connect[1], controller, this);
                        outputStream.writeUTF("" + newPort);
                        outputStream.flush();
                        controller.updateLog("New connect: \n" + connect[1] + " connected to " + newPort);
                    }
                }
            }
        }catch (Exception e){
            controller.updateLog("Server crush!");
        }
        finally {
            try {
                socket.close();
                serverSocket.close();
                controller.startAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateInterface(){
        controller.serverStatus = true;
        controller.startButton.setDisable(true);
        controller.updateLog("Server started");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.labelServerIP.setText(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                controller.labelServerPort.setText("" + mainPort);
                controller.labelServerStatus.setText("Online");
            }
        });
    }
}
