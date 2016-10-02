package server;
/**
 * Created by Виктор on 27.09.2016.
 */
public class User {

    private String name = null;
    private String password = null;
    private float balance = 0;
    private String clientPassword;

    User(String name, String password, String balance){
        this.name = name;
        this.password = password;
        this.balance = Float.parseFloat(balance);
    }



    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getBalance(){return ("" + this.balance);}
    
    

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
