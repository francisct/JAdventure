package com.jadventure.game;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadventure.game.GameModeData;
import com.jadventure.game.GameModeData.GameMode;
import com.jadventure.runtime.Client;
import com.jadventure.runtime.LocalIOHandler;
import com.jadventure.runtime.Server;
import com.jadventure.runtime.ServiceLocator;
import com.jadventure.game.menus.MainMenu;


public class JAdventure {
    
    public static void main(String[] args) {
        logger.info("Starting JAdventure...");
        logger.info("Received command-line arguments: " + Arrays.toString(args));

        GameModeData gameModeData = GameModeData.constructFromArguments(args);

        InitializeApp(gameModeData);
    }

    private static void InitializeApp(GameModeData gameModeData) {
        GameMode mode = gameModeData.getGameMode();

        if (GameMode.CLIENT == mode) {
            ServiceLocator.provide(new LocalIOHandler());
            Client client = new Client(gameModeData);
            client.listen();
        } else if (GameMode.SERVER == mode) {
            Server server = new Server(gameModeData);
            server.run();
        } else {
            ServiceLocator.provide(new LocalIOHandler());
            MainMenu menu = new MainMenu();
            //menu.show();
            menu.start();
        }
    }

    private static Logger logger = LoggerFactory.getLogger(JAdventure.class);
}