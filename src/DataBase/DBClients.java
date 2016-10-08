package DataBase;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Виктор on 08.10.2016.
 */
public class DBClients{
    private ArrayList<User> dbClients = new ArrayList<>();

    public DBClients(){
        try{
            File dbFile = new File("ClientsBase.dbc");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile.getName()));
            try {
                dbClients = (ArrayList<User>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();

        }catch (FileNotFoundException e){
            File dbFile = new File("ClientsBase.dbc");
            try {
                dbClients.add(new User("Admin", "1234", "2000"));
                dbClients.add(new User("Viktor", "4321", "4000"));
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile.getName()));
                oos.writeObject(dbClients);
                oos.flush();
                oos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }catch (IOException e){}
    }

    public ArrayList<User> getDBClients(){
        return dbClients;
    }

    public User getUser(String name){
        User u = null;
        for (int i = 0; i < dbClients.toArray().length; i++) {
            if(dbClients.get(i).getName().equals(name)) {
                u = dbClients.get(i);
                break;
            }
        }
        return u;
    }

    public boolean setUser(User u){
        boolean b = true;
        for (int i = 0; i < dbClients.toArray().length; i++) {
            if (dbClients.get(i).getName().equals(u.getName())) {b = false; break;}
        }
        if (b) {
            dbClients.add(u);
            updateBase();
        }
        return b;
    }

    public boolean deleteUser(String name){
        boolean b = false;
        for (int i = 0; i < dbClients.toArray().length; i++) {
            if (dbClients.get(i).getName().equals(name)) {
                dbClients.remove(i);
                updateBase();
                b = true;
                break;}
        }
        return b;
    }

    private void updateBase(){
        File dbFile = new File("ClientsBase.dbc");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile.getName()));
            oos.writeObject(dbClients);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
