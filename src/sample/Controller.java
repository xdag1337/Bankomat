package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {

    User user;

    @FXML
    Button loginButton, logoutButton, transactionButton, sendButton;
    @FXML
    TextField userName, userPassword,sendToName, sendSum, sendPassvord;
    @FXML
    Label labelName, labelBalance, labelMessage, labelPassword;
    @FXML
    GridPane userLayout, transactionLayout;
    @FXML
    CheckBox chekcRem;

    @FXML
    public void actionLogin(){
        if(!userName.getText().equals("") && !userPassword.getText().equals("")) {
            if(!checkInteger(userPassword.getText())) labelPassword.setText("Only numbers");
            else {
                labelPassword.setText("");
                user = new User(userName.getText(), Integer.parseInt(userPassword.getText()));
                labelName.setText(user.getName());
                labelBalance.setText("" + user.getBalance());
                userLayout.setVisible(true);
                labelMessage.setText("Login is Ok!!!");
                loginButton.setDisable(true);
                logoutButton.setDisable(false);
            }
        }else labelMessage.setText("Set login or password!!!");
    }
    @FXML
    public void actionLogout(){

        if (chekcRem.isSelected()){
            userLayout.setVisible(false);
            transactionLayout.setVisible(false);
            loginButton.setDisable(false);
            logoutButton.setDisable(true);
            labelMessage.setText("You Login Out.");
        }else{
            userName.setText("");
            userPassword.setText("");
            userLayout.setVisible(false);
            transactionLayout.setVisible(false);
            loginButton.setDisable(false);
            logoutButton.setDisable(true);
            labelMessage.setText("You Login Out.");
        }
    }
    @FXML
    public void actionTransaction(){
        if(Float.parseFloat(labelBalance.getText()) == 0) labelMessage.setText("Your balance is empty!");
        else {
            transactionLayout.setVisible(true);
            transactionButton.setDisable(true);
            sendToName.setText("");
            sendSum.setText("");
            sendPassvord.setText("");
            labelMessage.setText("Transaction create!");
        }
    }
    @FXML
    public void actionSend(){
        if(sendToName.getText().equals("") || sendSum.getText().equals("") || sendPassvord.getText().equals(""))
            labelMessage.setText("Fields is empty!");
        else if (!checkInteger(sendSum.getText()) && !checkInteger(sendPassvord.getText())){
                labelMessage.setText("Wrong type Sum or Password");
            }else if(Integer.parseInt(sendPassvord.getText()) == user.getPassword()) {
                        if (transaction()) {
                            labelMessage.setText("Transaction succesful.");
                            transactionLayout.setVisible(false);
                            transactionButton.setDisable(false);
                        }
            } else labelMessage.setText("Incorect password");
    }

    private boolean transaction(){


        // РЕАЛИЗАЦИЯ ПЕРЕВОДА


        return true;
    }

    private boolean checkInteger(String s){
        try{
            int i = Integer.parseInt(s);
            return  true;
        }catch (Exception e){return false;}
    }
}
