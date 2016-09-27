package server;
/**
 * Created by Виктор on 27.09.2016.
 */
public class User {

    private String name = null;
    private String password = null;

    User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
}
