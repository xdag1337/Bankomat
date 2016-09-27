package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    private Thread mainThread;
    private int port = 1111;
    private StringBuilder log = new StringBuilder();

    @FXML Button loginButton, logoutButton, transactionButton, sendButton, connectButton;
    @FXML TextField userName, userPassword,sendToName, sendSum, sendPassvord, serverIP;
    @FXML Label labelName, labelBalance, labelPassword;
    @FXML GridPane userLayout, transactionLayout;
    @FXML CheckBox chekcRem;
    @FXML TextArea areaLog;

    @FXML public void actionConnect()  {  //активность кнопки Connect
        if(serverIP.getText().equals("")) serverIP.setText("localhost"); //значение IP адреса, если поле пустое
        mainThread = new Thread(){  //новый поток для обмена данными с сервером
            private DataOutputStream dataOutput;
            private DataInputStream dataInput;
            public String answer = "connected"; //переменная, содержит в себе запрос на сервер, изменяется взависимости ответа от сервера

            @Override
            public void run(){
                try{
                    InetAddress address = InetAddress.getByName(serverIP.getText());
                    Socket socket = new Socket(address, port);
                    updateLog("Client start");
                    dataInput = new DataInputStream(socket.getInputStream());
                    dataOutput = new DataOutputStream(socket.getOutputStream());
                    dataOutput.writeUTF("connected");                 //первый запрос на сервер
                    dataOutput.flush();
                    while (true){
                        answer = dataInput.readUTF();        //принимаем ответ
                        dataOutput.writeUTF(queryHandler(answer));       //обрабатывает ответ и формирует новый запрос
                        dataOutput.flush();
                    }
                }catch (Exception e){}
            }

        };
        mainThread.start();
    }
    @FXML public void actionLogin(){

    }

    @FXML public void actionLogout(){
    }
    @FXML public void actionTransaction(){
    }
    @FXML public void actionSend(){
    }

    private String queryHandler(String answer) {  //обработчик ответов от сервера
        String temp = null;
        switch (answer){
            case "wait": temp = "wait"; break;
            case "connected": updateLog("Client connected to " + serverIP.getText());
                connectButton.setDisable(true);
                loginButton.setDisable(false);
                temp = "wait";
                break;
            case "getLogin":temp = userName.getText() + " " + userPassword.getText(); break;
            case "loginOK": updateLog("Login is correct"); userLayout.setVisible(true); temp = "wait"; break;
            case "errorLogin": updateLog("Login is incorrect"); temp = "wait"; break;
            default: break;
        }
        return temp;
    }
    private void updateLog(String message){
        log.append(message + "\n");
        areaLog.setText(log.toString());
    }
}
