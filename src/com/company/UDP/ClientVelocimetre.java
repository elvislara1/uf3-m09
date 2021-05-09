package com.company.UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {

    private int contador;
    private int mediana;
    private MulticastSocket multisocket;

    boolean continueConnected = true;

    InetSocketAddress groupMulticast;
    NetworkInterface netIf;

    public ClientVelocimetre() {
        try {
            multisocket = new MulticastSocket(5557);
            InetAddress multicastIP = InetAddress.getByName("224.0.3.31");
            groupMulticast = new InetSocketAddress(multicastIP, 5557);
            netIf = NetworkInterface.getByName("wlp0s20f3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[4];
        multisocket.joinGroup(groupMulticast, netIf);
        while (continueConnected) {
            DatagramPacket mpacket = new DatagramPacket(receivedData, 4);
            multisocket.receive(mpacket);
            mediana(mpacket.getData());
        }
        multisocket.leaveGroup(groupMulticast, netIf);
        multisocket.close();
    }

    private void mediana(byte[] data) {
        ByteBuffer.wrap(data).getInt();
        mediana += ByteBuffer.wrap(data).getInt();

        contador++;
        if (contador == 5) {
            System.out.println("Mediana: " + mediana / 5);
            //para!
            if (mediana / 5 > 60) {
                continueConnected = false;
            }
            contador = 0;
            mediana = 0;
        }
    }

    public static void main(String[] args) {
        ClientVelocimetre cVelocimetre = new ClientVelocimetre();
        try {
            cVelocimetre.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
