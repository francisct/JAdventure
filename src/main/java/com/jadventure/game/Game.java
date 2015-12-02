package com.jadventure.game;

import com.jadventure.game.entities.Player;
import com.jadventure.game.monsters.Monster;
import com.jadventure.game.monsters.MonsterFactory;
import com.jadventure.game.notification.DeathObserver;
import com.jadventure.game.repository.LocationRepository;
import com.jadventure.runtime.ServiceLocator;
import com.jadventure.game.prompts.CommandParser;

import java.util.ArrayList;

/**
 * This class contains the main loop that takes the input and
 * does the according actions.
 */
public class Game {
    public ArrayList<Monster> monsterList = new ArrayList<Monster>();
    public MonsterFactory monsterFactory = new MonsterFactory(); 
    public CommandParser parser;
    public Monster monster;
    Player player = null;

    public Game(Player player, String playerType)  {
          this.parser = new CommandParser(player);
          this.player = player;
          switch (playerType) {
              case "new":
                  newGameStart(player);
                  break;
              case "old":
                  ServiceLocator.getIOHandler().sendOutput("Welcome back, " + player.getName() + "!");
                  ServiceLocator.getIOHandler().sendOutput("");
                  player.getLocation().print();
                  gamePrompt(player);
                  break;
              default:
                  ServiceLocator.getIOHandler().sendOutput("Invalid player type");
                  break;
          }
    }
   
    /**
     * Starts a new game.
     * It prints the introduction text first and asks for the name of the player's
     * character and welcomes him / her. After that, it goes to the normal game prompt.
     */
    public void newGameStart(Player player) throws DeathException {
        ServiceLocator.getIOHandler().sendOutput(player.getIntro());
        String userInput = ServiceLocator.getIOHandler().getInput();
        player.setName(userInput);
        LocationRepository locationRepo = GameBeans.getLocationRepository(player.getName());
        this.player.setLocation(locationRepo.getInitialLocation());
        player.save();
        ServiceLocator.getIOHandler().sendOutput("Welcome to Silliya, " + player.getName() + ".");
        player.getLocation().print();
        gamePrompt(player);
    }



    /**
     * This is the main loop for the player-game interaction. It gets input from the
     * command line and checks if it is a recognised command.
     *
     * This keeps looping as long as the player didn't type an exit command.
     */


    public void gamePrompt(Player player)   {
        boolean continuePrompt = true;
       
            while (continuePrompt) {
                ServiceLocator.getIOHandler().sendOutput("\nPrompt:");
                String command = ServiceLocator.getIOHandler().getInput().toLowerCase();
                continuePrompt = parser.parse(player, command);
            }

    }
}
