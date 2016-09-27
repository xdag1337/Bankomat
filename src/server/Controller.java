package server;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller {
    private Thread mainThread;
    private int port = 1111;
    private StringBuilder log = new StringBuilder();
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private String query = null;
    private ServerFunction sf = new ServerFunction();

    @FXML Button startButton;
    @FXML TextArea areaLog;

    @FXML public void startAction(){
        mainThread = new Thread(){
            @Override
            public void run(){
                try{
                    serverSocket = new ServerSocket(port);
                    updateLog("Server start");
                    socket = serverSocket.accept();
                    dataInput = new DataInputStream(socket.getInputStream());
                    dataOutput = new DataOutputStream(socket.getOutputStream());
                    while (true){
                        query = dataInput.readUTF();
                        dataOutput.writeUTF(queryHandler(query));
                        dataOutput.flush();
                    }

                }catch (Exception e){}
            }
        };
        mainThread.start();
    }

    private String queryHandler(String queryFromClient) {
        String temp = null;
        try{
            switch (queryFromClient){
                case "wait": temp = "wait";
                        break;
                case "connected": updateLog("Client connected");
                        temp = "connected"; break;
                case "login": temp = "getLogin";
                    dataOutput.writeUTF(temp);
                    dataOutput.flush();
                    String[] logpas = dataInput.readUTF().split(" ");
                    if(sf.chekPassword(logpas[0], logpas[1])) {
                        temp = "loginOK";
                        updateLog(logpas[0] + " is login");
                    } else temp = "errorLogin"; break;
                default: temp = "pause"; break;
            }
        }catch (IOException e){}
        return temp;
    }

    private void updateLog(String message){
        log.append(message + "\n");
        areaLog.setText(log.toString());
    }
}