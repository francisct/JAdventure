package com.jadventure.runtime;

import java.net.*;
import java.io.*;

import com.jadventure.game.GameModeData;

public class Client {

    public Client(GameModeData gameModeData) {
        serverName = gameModeData.getServerName();
        port = gameModeData.getPort();

        ServiceLocator.provide(new LocalIOHandler());
    }

    public void Listen() {
        Socket client = null;

        try {
            client = new Socket(serverName, port);

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            while (true) {
                String serverMessage = in.readUTF();

                if (serverMessage.endsWith("END")) {
                    serverMessage = serverMessage.substring(0, serverMessage.length() - 3);
                    if (serverMessage.equals("QUERY")) {
                        String input = ServiceLocator.getIOHandler().getInput();
                        out.writeUTF(input);
                    } else if (serverMessage.equals("EXIT")) {
                        break;
                    } else {
                        ServiceLocator.getIOHandler().sendOutput(serverMessage);
                    }
                } else {
                    String message = "";
                    while (!serverMessage.endsWith("END")) {
                        message += serverMessage;
                        serverMessage = in.readUTF();
                    }
                    message = serverMessage.substring(0, serverMessage.length() - 3);
                    if (message.equals("QUERY")) {
                        String input = ServiceLocator.getIOHandler().getInput();
                        out.writeUTF(input);
                    } else if (serverMessage.equals("EXIT")) {
                        break;
                    } else {
                        ServiceLocator.getIOHandler().sendOutput(serverMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String serverName;
    private int port;
    
    private DataInputStream in;
    private DataOutputStream out;
}
