package com.jadventure.runtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkIOHandler implements IOHandler {

    public NetworkIOHandler(Socket socket) {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInput() {
        logger.debug("Querying Client for input");

        String input = null;

        String request = "QUERY";
        try {
            out.writeUTF(request + "END");
            input = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return input;
    }

    @Override
    public void sendOutput(String message) {
        logger.debug("Sending message to Client: " + message);
        try {
            out.writeUTF(message + "END");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DataOutputStream out;
    private static DataInputStream in;

    private static Logger logger = LoggerFactory.getLogger(NetworkIOHandler.class);
}
