package com.jadventure.game.notification;


import com.jadventure.game.DeathException;
import com.jadventure.game.Game;
import com.jadventure.game.QueueProvider;
import com.jadventure.game.entities.Player;
import com.jadventure.game.menus.MainMenu;

/**
 * Created by michal on 11/22/2015.
 */
public class DeathObserver implements IObserver {


    @Override
    public void update(boolean isAlive, Player player) throws DeathException {
        QueueProvider.offer("DEATH OBSERVER Player is alive : "+isAlive);
        prompt(player);
    }

    private void prompt(Player player) throws DeathException {
        QueueProvider.offer("You died... Start again? (y/n)");
        String reply = QueueProvider.take().toLowerCase();
        while (!reply.startsWith("y") && !reply.startsWith("n")) {
            QueueProvider.offer("You died... Start again? (y/n)");
            reply = QueueProvider.take().toLowerCase();
        }

        if (reply.startsWith("y")) {
            //Game.Replay();
            throw new DeathException("restart");

        } else if (reply.startsWith("n")) {
            //throw new DeathException("close");
            MainMenu.ExitGame();
        }
    }



}
