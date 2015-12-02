package com.jadventure.runtime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadventure.game.GameModeData;
import com.jadventure.game.menus.MainMenu;

public class Server {

    public Server(GameModeData gameMode) {
        port = gameMode.getPort();
    }

    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                acceptConnections(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void acceptConnections(ServerSocket listener) throws IOException {
        Socket client = listener.accept();

        logger.info("Client connected: " + client.getInetAddress().getCanonicalHostName());

        Thread thread = new Thread(new ClientHandler(client));
        thread.start();
    }

    private class ClientHandler implements Runnable {
        
        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            logger.debug("Server thread started with name: " + Thread.currentThread().getName());

            ServiceLocator.provide(new NetworkIOHandler(client));

            MainMenu menu = new MainMenu();
            //menu.show();
            menu.start();
        }

        private Socket client;
    }

    private int port;

    private static Logger logger = LoggerFactory.getLogger(Server.class);
}
