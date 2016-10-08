package Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Created by Виктор on 08.10.2016.
 */
public class Controller {
    private StringBuilder log = new StringBuilder();

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
    @FXML TextField serverIP, serverPort, labelName, labelBalance;
    @FXML Label labelPassword;
    @FXML GridPane userLayout;
    @FXML GridPane transactionLayout;
    @FXML CheckBox chekcRem;
    @FXML TextArea areaLog;

    @FXML public void actionConnect()  {
        new MainProcess(this);
    }
    @FXML public void actionLogin() throws IOException {
        if (userName.getText().equals("") || userPassword.getText().equals("")) updateLog("Set login and password");
        else ClientProcess.sendQuery("auth/" + userName.getText() + "/" + userPassword.getText());
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
        //updateLog("You logout");
    }
    @FXML public void actionTransaction(){
        if (Float.parseFloat(labelBalance.getText()) > 0) {
            updateLog("New transaction");
            sendToName.setText("");
            sendSum.setText("");
            sendPassvord.setText("");
            transactionLayout.setVisible(true);
        }else updateLog("Low balance");
    }
    @FXML public void actionSend() throws IOException {

        if(sendToName.getText().equals("") || sendSum.getText().equals("") || sendPassvord.getText().equals("")) updateLog("Fields is empty");
        else if(sendToName.getText().equals(userName.getText())) updateLog("Wrong user name");
        else if(!sendPassvord.getText().equals(userPassword.getText())) updateLog("Wrong pin");
        else if(Float.parseFloat(sendSum.getText()) > Float.parseFloat(labelBalance.getText())) updateLog("Wrong sum");
        else ClientProcess.sendQuery("tra/" + userName.getText() +"/" + userPassword.getText() + "/" + sendToName.getText() + "/" + sendSum.getText());
    }
    public void updateLog(String message){
        if (log.toString().equals("")) log.append(message);
        else log.append("\n"+message);
        areaLog.setText(log.toString());
    }
}
