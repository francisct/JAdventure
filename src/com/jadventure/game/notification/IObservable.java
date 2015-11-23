package com.jadventure.game.notification;


import com.jadventure.game.DeathException;

/**
 * Created by michal on 11/22/2015.
 */
public interface IObservable{

    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers() throws DeathException;
}
