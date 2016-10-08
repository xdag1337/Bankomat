package Server;

import DataBase.DBClients;
import DataBase.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Виктор on 08.10.2016.
 */
public class Controller {

    public boolean serverStatus = false;
    private StringBuilder log = new StringBuilder();
    public DBClients dbClients = new DBClients();
    public Map<String, DataOutputStream> clietnThreads = new HashMap<>();

    @FXML
    Button startButton, stopButton;
    @FXML
    TextArea areaLog;
    @FXML
    TextField infaChel, clientName, clientPassword, clientBalance;
    @FXML
    Label infaName, infaBalance, infaStatus, labelServerStatus, labelServerIP, labelServerPort;

    public void startAction(){
       new MainProcess(this);
    }

    public void updateLog(String message){
        log.append(message + "\n");
        areaLog.setText(log.toString());
    }

    public void showClient() {

        if (infaChel.getText().equals("")){
            updateLog("Field is empty");
        }else {
            User u = dbClients.getUser(infaChel.getText());
            if (u == null) updateLog("Client not found");
            else{
                infaName.setText(u.getName());
                infaBalance.setText("" + u.getBalance());
            }
        }
    }

    public void addClient() {
        if(clientName.getText().equals("")||clientPassword.getText().equals("")||clientBalance.getText().equals("")){
            updateLog("Filds is empty");
        }else{
            dbClients.setUser(new User(clientName.getText(), clientPassword.getText(), clientBalance.getText()));
            updateLog("Client is add");
            clientName.setText("");
            clientPassword.setText("");
            clientBalance.setText("");
        }
    }

    public void delClient() {
        if(clientName.getText().equals("")||clientPassword.getText().equals("")){
            updateLog("Filds is empty");
        }else if (dbClients.getUser(clientName.getText()) == null) {
            updateLog("Client not found");
        }else if (!dbClients.getUser(clientName.getText()).getPassword().equals(clientPassword.getText())) {
            updateLog("Wrong password");
        }else {
            dbClients.deleteUser(clientName.getText());
            updateLog("Client is delete");
            clientName.setText("");
            clientPassword.setText("");
            clientBalance.setText("");
        }
    }

    public void correction() {

//        for (int i = 0; i < sf.clientsBase.toArray().length; i++){
//            if (sf.clientsBase.get(i).getName().equals(clientName.getText())){
//                if (sf.clientsBase.get(i).getPassword().equals(clientPassword.getText())){
//                    updateLog("Новый пароль должен отличаться от старого!");
//                }else if(clientPassword.getText().equals("")){
//                    updateLog("Введите новый пароль");
//                }else{
//                    sf.clientsBase.get(i).setPassword(clientPassword.getText());
//                    updateLog("Пароль изменен");
//                }
//            }else if(i == sf.clientsBase.toArray().length-1){
//                updateLog("Неверное имя клиента");
//            }
//        }
//        for (int i = 0; i < sf.clientsBase.toArray().length; i++) {
//            if (sf.clientsBase.get(i).getName().equals(clientName.getText())) {
//                if ((""+sf.clientsBase.get(i).getBalance()).equals(clientBalance.getText())){
//                    updateLog("Новый баланс должен отличаться от старого!");
//                }else if(clientBalance.getText().equals("")){
//                    updateLog("Введите новый баланс!");
//                }else {
//                    sf.clientsBase.get(i).setBalance(Float.parseFloat(clientBalance.getText()));
//                    updateLog("Баланс изменен");
//                }
//            } else if (i == sf.clientsBase.toArray().length - 1) {
//                updateLog("Неверное имя клиента");
//            }
//        }
    }

    public void stopServer() {
    }
}
