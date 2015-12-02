package com.jadventure.game.notification;


import com.jadventure.game.Game;
import com.jadventure.game.entities.Player;
import com.jadventure.game.menus.MainMenu;
import com.jadventure.runtime.ServiceLocator;

/**
 * Created by michal on 11/22/2015.
 */
public class DeathObserver implements IObserver {


    @Override
    public void update(boolean isAlive, Player player) {
        //player died
        prompt(player);
    }

    private void prompt(Player player)  {
        ServiceLocator.getIOHandler().sendOutput("You died... Start again? (y/n)");
        String reply = ServiceLocator.getIOHandler().getInput();
        while (!reply.startsWith("y") && !reply.startsWith("n")) {
            ServiceLocator.getIOHandler().sendOutput("You died... Start again? (y/n)");
            reply = ServiceLocator.getIOHandler().getInput();
        }
        if (reply.startsWith("n")) {
            //throw DEathexception close
            MainMenu.ExitGame();
        }

        else if (reply.startsWith("y")) {
            //do nothing
            //restart just goes to the next iteration of while loop (never exits). IN MAINMENU.JAVA
        }
    }



}
