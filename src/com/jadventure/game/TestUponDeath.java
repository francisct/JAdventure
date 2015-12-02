package com.jadventure.game;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jadventure.game.entities.Player;
import com.jadventure.game.monsters.Wolf;
import com.jadventure.game.notification.DeathObserver;

public class TestUponDeath {
    
    @Test
    public void testDeathRecruit() {
    	System.out.println("Creating a new player of type recruit and adding a DeathObserver.");
        Player player = Player.getInstance("recruit");
        player.addObserver(new DeathObserver());

        System.out.println("Testing character type, health, and level.");
        assertEquals(player.getCurrentCharacterType(), "Recruit");
        assertEquals(player.getHealth(), 100);
        assertEquals(player.getLevel(), 1);
        
        System.out.println("Setting character level to 2.");
        player.setLevel(2);
        assertEquals(player.getLevel(), 2);
        
        System.out.println("Creating new monster of type Wolf.");
        Wolf wolf = new Wolf(player.getLevel());
        assertEquals(wolf.monsterType, "Wolf");
        
        System.out.println("Wolf damaging the player once with 20 damage.");
        player.setHealth((int)(player.getHealth() - wolf.getDamage()));
        assertEquals(player.getHealth(), 80);
        
        System.out.println("Wolf damaging the player a second time with 20 damage.");
        player.setHealth((int)(player.getHealth() - wolf.getDamage()));
        assertEquals(player.getHealth(), 60);
        
        System.out.println("Wolf damaging the player a third time with 20 damage.");
        player.setHealth((int)(player.getHealth() - wolf.getDamage()));
        assertEquals(player.getHealth(), 40);
        
        System.out.println("Wolf damaging the player a fourth time with 20 damage.");
        player.setHealth((int)(player.getHealth() - wolf.getDamage()));
        assertEquals(player.getHealth(), 20);
    }

    @Test
    public void testDeathSewerrat() {
        	
    }
}
