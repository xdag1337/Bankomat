package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static ServerFunction sf = new ServerFunction();

    private static StringBuilder log = new StringBuilder();

    @FXML Button startButton, stopButton;
    @FXML static TextArea areaLog;
    @FXML TextField infaChel, clientName, clientPassword, clientBalance;
    @FXML Label infaName, infaBalance, infaStatus;
    @FXML public void startAction(){
        new ServerThread();
    }

    private static void updateLog(String message){
        log.append(message + "\n");
        areaLog.setText(log.toString());
    }

    public void showClient() {
        User user1 = null;
        for (int i = 0; i < sf.clientsBase.toArray().length; i++) {
            if (sf.clientsBase.get(i).getName().equals(infaChel.getText())){
                infaName.setText(sf.clientsBase.get(i).getName());
                infaBalance.setText(sf.clientsBase.get(i).getBalance());
            }else if (i == sf.clientsBase.toArray().length-1){
                updateLog("User not found");
                infaName.setText("");
                infaBalance.setText("");
            }

        }

    }


    public void addClient() {
        if(clientName.getText().equals("")||clientPassword.getText().equals("")||clientBalance.getText().equals("")){
            updateLog("Все поля обязательны для заполнения!");
        }else{
        sf.clientsBase.add(new User(clientName.getText(),clientPassword.getText(),clientBalance.getText()));
            updateLog("Создан новый клиент");

        }
    }

    public void delClient() {
        for (int i = 0; i < sf.clientsBase.toArray().length; i++) {
            if (sf.clientsBase.get(i).getName().equals(clientName.getText())){
                sf.clientsBase.remove(i);
                updateLog("Клиент удален");
            }else if (i == sf.clientsBase.toArray().length-1){
                updateLog("Клиент не найден");
            }
        }
    }


    public void correction() {

        for (int i = 0; i < sf.clientsBase.toArray().length; i++){
            if (sf.clientsBase.get(i).getName().equals(clientName.getText())){
                if (sf.clientsBase.get(i).getPassword().equals(clientPassword.getText())){
                    updateLog("Новый пароль должен отличаться от старого!");
                }else if(clientPassword.getText().equals("")){
                    updateLog("Введите новый пароль");
                }else{
                sf.clientsBase.get(i).setPassword(clientPassword.getText());
                updateLog("Пароль изменен");
                }
            }else if(i == sf.clientsBase.toArray().length-1){
                updateLog("Неверное имя клиента");
            }
        }
        for (int i = 0; i < sf.clientsBase.toArray().length; i++) {
            if (sf.clientsBase.get(i).getName().equals(clientName.getText())) {
                if (sf.clientsBase.get(i).getBalance().equals(clientBalance.getText())){
                    updateLog("Новый баланс должен отличаться от старого!");
                }else if(clientBalance.getText().equals("")){
                    updateLog("Введите новый баланс!");
                }else {
                    sf.clientsBase.get(i).setBalance(Float.parseFloat(clientBalance.getText()));
                    updateLog("Баланс изменен");
                }
            } else if (i == sf.clientsBase.toArray().length - 1) {
                updateLog("Неверное имя клиента");
            }
        }
    }

    public void stopServer() {
    }

    private static class ServerThread implements Runnable{
        int th = 0;
        static int index = 0;
        static  Map<Integer, Integer> sevPorts = new HashMap();
        static int sevPort = 1110;
        Thread sevThread;
        DataInputStream dataInput;
        DataOutputStream dataOutput;
        String query = null;
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

//        @Override
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
                            temp = "00";//sf.getUserBalance(logpas[0], logpas[1]);
                            dataOutput.writeUTF(temp);
                            dataOutput.flush();
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