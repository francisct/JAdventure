package com.jadventure.game.notification;

import com.jadventure.game.entities.Player;

/**
 * Created by michal on 11/22/2015.
 */
public interface IObserver  {

    void update(boolean isAlive, Player player);
}
