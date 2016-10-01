package server;

import java.util.ArrayList;

/**
 * Created by Виктор on 27.09.2016.
 *
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

    public boolean tranzaction(String client1, String client2, float suma){
        float temp1 = 0;
        float temp2 = 0;
        User user1 = null;
        User user2 = null;
        try {
            for (User user : clientsBase) {
                if (user.getName().equals(client1)) {
                    temp1 = Float.parseFloat(user.getBalance());
                    user1 = user;
                } else {
                    return false;
                }
                if (user.getName().equals(client2)) {
                    temp2 = Float.parseFloat(user.getBalance());
                    user2 = user;
                } else {
                    return false;
                }
            }

            user1.setBalance(temp1-suma);
            user2.setBalance(temp2+suma);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
