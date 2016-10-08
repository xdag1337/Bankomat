package Server;

import DataBase.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Виктор on 06.10.2016.
 */
public class ServerProcess implements Runnable {
    private Thread thread = null;
    private Controller controller = null;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private MainProcess mainProcess = null;
    private String nameUser = null;

    private int port = 0;

    ServerProcess(int port, String nameUser, Controller controller, MainProcess mainProcess){
        this.mainProcess = mainProcess;
        this.controller = controller;
        this.port = port;
        this.nameUser = nameUser;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String[] query = inputStream.readUTF().split("/");
                switch (query[0]){
                    case "auth":
                        if (checkUser(query[1], query[2])) {
                            controller.updateLog(query[1] + " connected");
                            sendQuery("auth/ok");
                        } else sendQuery("auth/err/usNF");
                        break;

                    case "bal":
                        if(checkUser(query[1], query[2])){
                        sendQuery("bal/" + checkBalance(query[1]));
                        }else sendQuery("bal/err");
                        break;

                    case "tra":
                        sendQuery("tra/" + transaction(query[1], query[3], query[4]));
                        sendQuery("bal/" + controller.dbClients.getUser(query[1]).getBalance());
                        if (controller.clietnThreads.containsKey(query[3]))
                            sendQuery("bal/" + controller.dbClients.getUser(query[3]).getBalance(), query[3]);
                        break;

                    default: break;
                }
            }
        }catch (Exception e){
            controller.updateLog("Port " + port + " disconnected\n" + e.toString());
        }
    }

    private void sendQuery(String q) {
        try {
            outputStream.writeUTF(q);
            outputStream.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    private void sendQuery(String q, String nameUser) {
        try {
            controller.clietnThreads.get(nameUser).writeUTF(q);
            controller.clietnThreads.get(nameUser).flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    private boolean checkUser(String name, String password){
        boolean b = false;
        User u = controller.dbClients.getUser(name);
        if (u.getName().equals(name)){
            if (u.getPassword().equals(password)) b = true;
        }
        return b;
    }
    private String checkBalance(String name){
        return "" + controller.dbClients.getUser(name).getBalance();
    }
    private String transaction(String nameUserSender, String nameUserRecip, String sumTransaction){
        String string = "err/unc";
        User uSender = controller.dbClients.getUser(nameUserSender);
        User uRecip = controller.dbClients.getUser(nameUserRecip);
        float bSender = controller.dbClients.getUser(nameUserSender).getBalance();
        float bRecip = controller.dbClients.getUser(nameUserRecip).getBalance();
        float summ = Float.parseFloat(sumTransaction);

        if (uSender == null || uRecip == null) {
            string = "err/usrNF";
        }else if (summ > controller.dbClients.getUser(nameUserSender).getBalance()) {
            string = "err/lowB";
        }else{
            uSender.setBalance(bSender - summ);
            uRecip.setBalance(bRecip + summ);
            string = "ok";
        }
        return string;
    }
}
