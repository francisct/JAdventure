package com.jadventure.game.notification;


import com.jadventure.game.Game;
import com.jadventure.game.QueueProvider;
import com.jadventure.game.entities.Player;
import com.jadventure.game.menus.MainMenu;

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
        QueueProvider.offer("You died... Start again? (y/n)");
        String reply = QueueProvider.take().toLowerCase();
        while (!reply.startsWith("y") && !reply.startsWith("n")) {
            QueueProvider.offer("You died... Start again? (y/n)");
            reply = QueueProvider.take().toLowerCase();
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
