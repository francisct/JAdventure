package com.jadventure.game;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jadventure.game.entities.Player;
import com.jadventure.game.notification.DeathObserver;

public class TestUponDeath {
    
    @Test
    public void testDeathRecruit() {
    	System.out.println("Create a new player of type recruit and add a DeathObserver.");
        Player player = Player.getInstance("recruit");
        player.addObserver(new DeathObserver());

        System.out.println("Testing character type");
        assertEquals(player.getCurrentCharacterType(), "Recruit");
        assertEquals(player.getHealth(), 100);
        assertEquals(player.getLevel(), 1);
        
        player.setLevel(2);
        assertEquals(player.getLevel(), 2);
        
        
    }

    @Test
    public void testDeathSewerrat() {
        	
    }
}
