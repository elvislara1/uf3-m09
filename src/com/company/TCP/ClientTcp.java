package com.company.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcp extends Thread {

    Scanner scanner = new Scanner(System.in);
    String hostname;
    int port;
    boolean continueConnected;
    int intents;
    //Llista llista;


    public ClientTcp(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
        intents=0;
    }

    public void run() {
        Llista serverData;
        Llista request;
        Socket socket;
        ObjectOutputStream ooStream;
        ObjectInputStream ioStream;

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            ooStream = new ObjectOutputStream(socket.getOutputStream());
            ioStream = new ObjectInputStream(socket.getInputStream());

            request = getRequest();
            ooStream.writeObject(request);
            ooStream.flush();
            System.out.println(request.getNom() + "...antes del sorted..." + request.getNumberList());

            serverData = (Llista) ioStream.readObject();
            System.out.println(serverData.getNom() + "...despues de que el servidor se encarge de hacer el sorted.... " + serverData.getNumberList());
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Llista getRequest() {
        List<Integer> integerList = new ArrayList<>();
        String nom;

        System.out.println("Introduce tu nombre para que sea añadida a la lista");
        nom = scanner.nextLine();

        for (int i = 0; i < 5; i++) {
            System.out.println("Introduce un numero (hasta 5 numeros)");
            integerList.add(scanner.nextInt());
        }
        return new Llista(nom, integerList);
    }

    private void close(Socket socket){
        //m09
        try {
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientTcp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ClientTcp clientTcp = new ClientTcp("localhost",5559);
        clientTcp.start();
    }
}