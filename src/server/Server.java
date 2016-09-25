package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Server side");

        BufferedReader in = null;
        PrintWriter out= null;

        BufferedReader in1 = null;//2
        PrintWriter out1= null;//2

        ServerSocket servers = null;
        Socket fromclient = null;

        ServerSocket servers1 = null;//2
        Socket fromclient1 = null;//2

// create server socket
        try {
            servers = new ServerSocket(4444);
            servers1 = new ServerSocket(4445);//2
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }

        try {
            System.out.print("Waiting for a client...");
            fromclient= servers.accept();
            System.out.println("Client port 4444");
            fromclient1= servers1.accept();
            System.out.println("Client port 4445");
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        }

        in = new BufferedReader(new InputStreamReader(fromclient.getInputStream()));
        out = new PrintWriter(fromclient.getOutputStream(),true);
        String input,output;


        in1 = new BufferedReader(new InputStreamReader(fromclient1.getInputStream()));
        out1 = new PrintWriter(fromclient1.getOutputStream(),true);
        String input1,output1;

        System.out.println("Wait for messages");
        Scanner scanner = new Scanner(System.in);

        while ((input = in.readLine()) != null & (input1 = in1.readLine()) != null) {

            System.out.println(input);
            System.out.println(input1);
            out.println("S to Client1::: "+scanner.nextLine());
            out1.println("S to Client2::: "+scanner.nextLine());
        }




        out.close();
        in.close();
        fromclient.close();
        servers.close();


        out1.close();
        in1.close();
        fromclient1.close();
        servers1.close();
    }
}
