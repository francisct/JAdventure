package com.jadventure.game.menus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.jadventure.game.Game;
import com.jadventure.game.JAdventure;

import com.jadventure.game.entities.Player;
import com.jadventure.game.notification.DeathObserver;
import com.jadventure.runtime.ServiceLocator;

/**
 * The first menu displayed on user screen
 *
 * @see JAdventure
 * This menu lets the player choose whether to load an exiting game,
 * start a new one, or exit to the terminal.
 */
public class MainMenu extends Menus {

    static boolean exitGamePlayerDied = false;

    
    public void show() {
        this.menuItems.add(new MenuItem("Start", "Starts a new Game", "new"));
        this.menuItems.add(new MenuItem("Load", "Loads an existing Game"));
        this.menuItems.add(new MenuItem("Delete", "Deletes an existing Game"));
        this.menuItems.add(new MenuItem("Exit", null, "quit"));


        while (true) {
            MenuItem selectedItem = displayMenu(this.menuItems);
            boolean continueGame = testOption(selectedItem);
            if (!continueGame || exitGamePlayerDied) {
                break;
            }
        }
      ServiceLocator.getIOHandler().sendOutput("EXIT");

    }

    public static void ExitGame() {
        exitGamePlayerDied = true;
    }

    private static boolean testOption(MenuItem m) {
        String key = m.getKey();
        switch (key) {
            case "start":
                try {
                    Path orig = Paths.get("json/original_data/locations.json");
                    Path dest = Paths.get("json/locations.json");
                    Files.copy(orig, dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ServiceLocator.getIOHandler().sendOutput("Unable to load new locations file.");
                    ex.printStackTrace();
                }
                new ChooseClassMenu();
                break;
            case "exit":
                ServiceLocator.getIOHandler().sendOutput("Goodbye!");
                return false;
            case "load":
                listProfiles();
                ServiceLocator.getIOHandler().sendOutput("\nWhat is the name of the avatar you want to load? Type 'back' to go back");
                Player player = null;
                boolean exit = false;
                while (player == null) {
                    key = ServiceLocator.getIOHandler().getInput();
                    if (Player.profileExists(key)) {
                        player = Player.load(key);
                        //register observer
                        player.addObserver(new DeathObserver());
                    } else if (key.equals("exit") || key.equals("back")) {
                        exit = true;
                        break;
                    } else {
                        ServiceLocator.getIOHandler().sendOutput("That user doesn't exist. Try again.");
                    }
                }
                if (exit) {
                    return true;
                }
                new Game(player, "old");
                break;
            case "delete":
                listProfiles();
                ServiceLocator.getIOHandler().sendOutput("\nWhich profile do you want to delete? Type 'back' to go back");
                exit = false;
                while (!exit) {
                    key = ServiceLocator.getIOHandler().getInput();
                    if (Player.profileExists(key)) {
                        String profileName = key;
                        ServiceLocator.getIOHandler().sendOutput("Are you sure you want to delete " + profileName + "? y/n");
                        key = ServiceLocator.getIOHandler().getInput();
                        if (key.equals("y")) {
                            File profile = new File("json/profiles/" + profileName);
                            deleteDirectory(profile);
                            ServiceLocator.getIOHandler().sendOutput("Profile Deleted");
                            return true;
                        } else {
                            listProfiles();
                            ServiceLocator.getIOHandler().sendOutput("\nWhich profile do you want to delete?");
                        }
                    } else if (key.equals("exit") || key.equals("back")) {
                        exit = true;
                        break;
                    } else {
                        ServiceLocator.getIOHandler().sendOutput("That user doesn't exist. Try again.");
                    }
                }
                break;
        }
        return true;
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    private static void listProfiles() {
        ServiceLocator.getIOHandler().sendOutput("Profiles:");
        File file = new File("json/profiles");
        String[] profiles = file.list();
        int i = 1;
        for (String name : profiles) {
            if (new File("json/profiles/" + name).isDirectory()) {
                ServiceLocator.getIOHandler().sendOutput("  " + i + ". " + name);
            }
            i += 1;
        }
    }
}
