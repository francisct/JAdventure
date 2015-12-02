package com.jadventure.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameModeData {
    
    public static enum GameMode {
        STAND_ALONE, CLIENT, SERVER
    }

    public static GameModeData constructFromArguments(String[] args) {
        return new GameModeData(args);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getPort() {
        return port;
    }

    public String getServerName() {
        return serverName;
    }

    private GameModeData(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {

        if (args == null || args.length == 0) {
            logger.info("No arguments passed. Running in stand-alone mode...");
            gameMode = GameMode.STAND_ALONE;
            return;
        }

        parseGameMode(args[0]);

        if (gameMode != GameMode.STAND_ALONE) {
            parseConnectionData(args);
        }
    }

    private void parseGameMode(String gameModeArgument) {
        gameModeArgument = gameModeArgument.trim().toUpperCase();

        try {
            gameMode = GameMode.valueOf(gameModeArgument);
            logger.info("Detected game mode: " + gameModeArgument);
        } catch (IllegalArgumentException e) {
            logger.error(gameModeArgument + " is not a known game mode.");

            logger.error("Terminating the application...");
            System.exit(-1);
        }
    }

    private void parseConnectionData(String[] args) {
        try {
            port = Integer.parseInt(args[1]);
            if (gameMode == GameMode.CLIENT) {
                serverName = args[2];
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            if (e instanceof ArrayIndexOutOfBoundsException) {
                logger.error("Invalid number of arguments for game mode " + gameMode);
            } else {
                logger.error("Invalid port number: " + args[1]);
            }

            logger.error("Terminating the application...");
            System.exit(-1);
        }
    }

    private GameMode gameMode;
    private int port;
    private String serverName;

    private static Logger logger = LoggerFactory.getLogger(GameModeData.class);
}
