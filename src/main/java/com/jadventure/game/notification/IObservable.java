package com.jadventure.game.notification;

/**
 * Created by michal on 11/22/2015.
 */
public interface IObservable{

    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}
