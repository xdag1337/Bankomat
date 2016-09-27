package server;

import java.util.ArrayList;

/**
 * Created by Виктор on 27.09.2016.
 */
public class ServerFunction {

    ArrayList<User> clientsBase = new ArrayList<>();

    ServerFunction(){
        clientsBase.add(new User("Admin", "1234"));
    }

    public int findUser(String name){
        int userIndex = -1;
        for (int i = 0; i < clientsBase.toArray().length; i++) {
            if (clientsBase.get(i).getName().equals(name)) userIndex = i; break;
        }
        return userIndex;
    }

    public boolean chekPassword(String name, String password){
        if(findUser(name) < 0) return false;
        else if(clientsBase.get(findUser(name)).getPassword().equals(password)) return true;
            else return false;
    }
}
