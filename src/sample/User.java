package sample;

/**
 * Created by vertex0008 on 24.09.2016.
 */
public class User {
    private String name = "";
    private int password = 0;
    private float balance = 0;

    User(String name, int password){
        this.name = name;
        this.password = password;
        balance = 10000;
    }

    public String getName(){return this.name;}
    public float getBalance(){return  this.balance;}
    public int getPassword(){return this.password;}

}
