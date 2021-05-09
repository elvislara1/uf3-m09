package com.company.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ThreadSevidorAdivina implements Runnable {

    /* Thread que gestiona la comunicació de SrvTcPAdivina.java i un cllient ClientTcpAdivina.java */

    Socket clientSocket = null;
    Llista msgEntrant;
    Llista msgSortint;

    boolean acabat;

    public ThreadSevidorAdivina(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        acabat = false;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            while(!acabat) {
                msgEntrant = (Llista)inFromClient.readObject();
                msgSortint = generarRespuesta(msgEntrant);
                outToClient.writeObject(msgSortint);

            }
        }catch(IOException | ClassNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Llista generarRespuesta(Llista msgIn) {
        System.out.println(msgIn.getNom());
        Llista ret = null;

        if (msgEntrant != null && msgEntrant.getNumberList().size() > 0) {
            ret = new Llista(msgEntrant.getNom(), msgEntrant.getNumberList().stream().sorted().distinct().collect(Collectors.toList()));
            acabat = true;
        }
        return ret;
    }
}