package com.company.TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcp {
    int port;
    Llista llista;

    public ServidorTcp(int port){
        this.port = port;
    }

    public void listen(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket= new ServerSocket(port);

            //esperar conexión del cliente y lanzar thread
            while (true){
                clientSocket = serverSocket.accept();
                //establecer
                ThreadSevidorAdivina threadServidorTcp = new ThreadSevidorAdivina(clientSocket);
                Thread client = new Thread(threadServidorTcp);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServidorTcp ServidorTcp = new ServidorTcp(5559);
        ServidorTcp.listen();
    }
}
