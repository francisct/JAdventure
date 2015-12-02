package com.jadventure.game;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jadventure.game.entities.Player;
import com.jadventure.game.notification.DeathObserver;

public class UponDeathTest {

    @Test
    public void testDeathRecruit() {
        Player player = Player.getInstance("recruit");
        player.addObserver(new DeathObserver());

        assertEquals(player.getCurrentCharacterType(), "Recruit");
        assertEquals(player.getHealth(), 100);
        assertEquals(player.getLevel(), 1);
    }

    @Test
    public void testDeathSewerrat() {
        	
    }
}
