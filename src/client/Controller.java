package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML Button loginButton, logoutButton, transactionButton, sendButton;
    @FXML TextField userName, userPassword,sendToName, sendSum, sendPassvord;
    @FXML Label labelName, labelBalance, labelMessage, labelPassword;
    @FXML GridPane userLayout, transactionLayout;
    @FXML CheckBox chekcRem;

    @FXML public void actionLogin(){
    }
    @FXML public void actionLogout(){
    }
    @FXML public void actionTransaction(){
    }
    @FXML public void actionSend(){
    }
}
