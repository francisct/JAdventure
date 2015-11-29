package com.jadventure.runtime;

import java.net.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import com.jadventure.game.GameModeData;

public class Client {

    public Client(GameModeData gameModeData) {
        hostname = gameModeData.getServerName();
        port = gameModeData.getPort();

        ServiceLocator.provide(new LocalIOHandler());
    }

    public void listen() {
        try {
            connect();
            processMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void connect() throws UnknownHostException, IOException {
        System.out.println("Connecting to " + hostname + ":" + port);
        server = new Socket(hostname, port);
        System.out.println("Connection Established");
    }

    public void processMessages() throws IOException {

        DataInputStream in = new DataInputStream(server.getInputStream());
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        
        while (true) {
            String serverMessage = in.readUTF();

            if (serverMessage.endsWith("END")) {
                serverMessage = serverMessage.substring(0, serverMessage.length() - 3);
                if (serverMessage.equals("QUERY")) {
                    String input = ServiceLocator.getIOHandler().getInput();
                    out.writeUTF(input);
                } else if (serverMessage.equals("EXIT")) {
                    logger.info("EXIT request recieved");
                    break;
                } else {
                    ServiceLocator.getIOHandler().sendOutput(serverMessage);
                }
            }
        }
    }

    private Socket server;

    private String hostname;
    private int port;

    private static Logger logger = LoggerFactory.getLogger(NetworkIOHandler.class);
}