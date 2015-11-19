package com.jadventure.runtime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.jadventure.game.GameModeData;
import com.jadventure.game.menus.MainMenu;

public class Server {

    public Server(GameModeData gameMode) {
        port = gameMode.getPort();
    }

    public void run() {
        while (true) {
            ServerSocket listener = null;
            try {
                listener = new ServerSocket(port);
                while (true) {
                    Socket server = listener.accept();

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            MainMenu menu = new MainMenu(server);
                            menu.start();
                        }
                    };
                 
                    new Thread(r).start();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException c) {
                c.printStackTrace();
            } finally {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int port;
}
