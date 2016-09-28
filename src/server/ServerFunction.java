package server;

import java.util.ArrayList;

/**
 * Created by Виктор on 27.09.2016.
 */
public class ServerFunction {

    ArrayList<User> clientsBase = new ArrayList<>();

    ServerFunction(){
        clientsBase.add(new User("Viktor", "4321", "10000"));
        clientsBase.add(new User("Admin", "1234", "2000"));
    }

    public int findUser(String name){
        int i = 0;
        for (; i < clientsBase.toArray().length; i++) {
            if (clientsBase.get(i).getName().equals(name)) break;
        }
        return i;
    }

    public boolean chekPassword(String name, String password){
        if(findUser(name) < 0) return false;
        else if(clientsBase.get(findUser(name)).getPassword().equals(password)) return true;
            else return false;
    }

    public String getUserBalance(String name, String password){
        if (chekPassword(name, password)) return clientsBase.get(findUser(name)).getBalance();
        else return "0,0";
    }
}
