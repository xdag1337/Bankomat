package DataBase;

import java.io.Serializable;

/**
 * Created by Виктор on 08.10.2016.
 */
public class User implements Serializable {

    private String name = null;
    private String password = null;
    private float balance = 0;

    public User(String name, String password, String balance){
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
    public float getBalance(){return (this.balance);}

    public void setBalance(float balance) {
        this.balance = balance;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}