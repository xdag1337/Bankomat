package client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Controller {
    private static boolean login = false;
    private static boolean trans = false;
    private Thread mainThread;
    private int port = 0;
    private StringBuilder log = new StringBuilder();
    public  String answer = "connected";//переменная, содержит в себе запрос на сервер, изменяется взависимости ответа от сервера
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;

    @FXML Button loginButton;
    @FXML Button logoutButton;
    @FXML Button transactionButton;
    @FXML Button sendButton;
    @FXML Button connectButton;
    @FXML TextField userName;
    @FXML TextField userPassword;
    @FXML TextField sendToName;
    @FXML TextField sendSum;
    @FXML TextField sendPassvord;
    @FXML TextField serverIP, serverPort;
    @FXML Label labelName, labelBalance, labelPassword;
    @FXML GridPane userLayout;
    @FXML GridPane transactionLayout;
    @FXML CheckBox chekcRem;
    @FXML TextArea areaLog;

    @FXML public void actionConnect()  {  //активность кнопки Connect
        port = Integer.parseInt(serverPort.getText());
        if(serverIP.getText().equals("")) serverIP.setText("localhost"); //значение IP адреса, если поле пустое
            mainThread = new Thread() {  //новый поток для обмена данными с сервером
                @Override
                public void run() {
                        try {
                            InetAddress address = InetAddress.getByName(serverIP.getText());
                            Socket socket = new Socket(address, port);
                            updateLog("Client start");
                            dataInput = new DataInputStream(socket.getInputStream());
                            dataOutput = new DataOutputStream(socket.getOutputStream());
                            dataOutput.writeUTF(answer);                 //первый запрос на сервер
                            dataOutput.flush();
                            while (true) {
                                answer = dataInput.readUTF();//принимаем ответ
                                dataOutput.writeUTF(queryHandler(answer));       //обрабатывает ответ и формирует новый запрос
                                dataOutput.flush();
                            }

                        } catch (Exception e) {
                            port++;
                        }
                    }
            };
            mainThread.start();
    }
    @FXML public void actionLogin(){
        updateLog("Login checking...");
        login = true;
    }

    @FXML public void actionLogout(){
        if (!chekcRem.isSelected()){
            userName.setText("");
            userPassword.setText("");
        }
        transactionLayout.setVisible(false);
        userLayout.setVisible(false);
        userName.setDisable(false);
        userPassword.setDisable(false);
        chekcRem.setDisable(false);
        loginButton.setDisable(false);
        logoutButton.setDisable(true);
        updateLog("You logout");
    }
    @FXML public void actionTransaction(){
        updateLog("New transaction");
        trans = true;
    }
    @FXML public void actionSend(){
    }

    private String queryHandler(String answer) throws IOException {  //обработчик ответов от сервера
        String temp = null;
        switch (answer){
            case "wait":    if(login) {
                                temp = "login";
                                login = false;
                            }else if(trans){
                                temp = "transaction";
                                trans = false;
                            }else temp = "wait"; break;

            case "connected":   updateLog("Client connected to " + serverIP.getText());
                                connectButton.setDisable(true);
                                loginButton.setDisable(false);
                                temp = "wait";break;

            case "getLogin":    temp = userName.getText() + " " + userPassword.getText(); break;

            case "loginOK":
                updateLog("Login is correct");
                userLayout.setVisible(true);
                loginButton.setDisable(true);
                logoutButton.setDisable(false);
                userName.setDisable(true);
                userPassword.setDisable(true);
                chekcRem.setDisable(true);
                temp = "wait";

            case "balance":   break;

            case "errorLogin":  updateLog("Wrong login or password");
                                temp = "wait"; break;
            case "transaction": transactionLayout.setVisible(true); temp = "wait"; break;
            default: break;
        }
        return temp;
    }
    private void updateLog(String message){
        if (log.toString().equals("")) log.append(message);
        else log.append("\n"+message);
        areaLog.setText(log.toString());
    }
}
