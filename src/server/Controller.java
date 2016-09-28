package server;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static StringBuilder log = new StringBuilder();

    @FXML Button startButton;
    @FXML static TextArea areaLog;

    @FXML public void startAction(){
        new ServerThread();
    }

    private static void updateLog(String message){
        log.append(message + "\n");
        areaLog.setText(log.toString());
    }

    private static class ServerThread implements Runnable{
        int th = 0;
        static int index = 0;
        static  Map<Integer, Integer> sevPorts = new HashMap<>();
        static int sevPort = 1110;
        Thread sevThread;
        DataInputStream dataInput;
        DataOutputStream dataOutput;
        String query = null;
        private ServerFunction sf = new ServerFunction();
        {
            while (true){
                if (sevPorts.containsKey(index)) index++;
                else {
                    th = index;
                    sevPorts.put(index, sevPort);
                    break;
                }
            }
            sevPort++;
        }

        ServerThread(){
            sevThread = new Thread(this, "" + System.nanoTime());
            sevThread.start();
        }
        @Override
        public synchronized void run() {
            if (sevPorts.containsKey(th)) {
                try {
                    ServerSocket serverSocket = new ServerSocket(sevPorts.get(th));
                    updateLog("Server wait on port " + sevPorts.get(th));
                    Socket socket = serverSocket.accept();
                    dataInput = new DataInputStream(socket.getInputStream());
                    dataOutput = new DataOutputStream(socket.getOutputStream());
                    while (true) {
                        query = dataInput.readUTF();
                        dataOutput.writeUTF(queryHandler(query));
                        dataOutput.flush();
                    }

                } catch (Exception e) {
                }
            }
        }

        private String queryHandler(String queryFromClient) {
            String temp = null;
            String[] logpas = new String[2];
            try {
                switch (queryFromClient) {
                    case "wait":
                        temp = "wait";
                        break;
                    case "connected":
                        updateLog("Client connected");
                        temp = "connected";
                        new ServerThread();
                        break;
                    case "login":
                        temp = "getLogin";
                        dataOutput.writeUTF(temp);
                        dataOutput.flush();
                        logpas = dataInput.readUTF().split(" ");
                        if (sf.chekPassword(logpas[0], logpas[1])) {
                            temp = "loginOK";
                            updateLog(logpas[0] + " is login");
                        } else temp = "errorLogin";
                        break;
                    case "balance":
                            //temp = sf.getUserBalance(logpas[0], logpas[1]);
                        break;
                    case "transaction": temp = "transaction"; break;
                    default:
                        temp = "pause";
                        break;
                }
            }catch(Exception e){}
            return temp;
        }
    }
}