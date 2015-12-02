package com.jadventure.game.menus;

import com.jadventure.game.entities.Player;
import com.jadventure.runtime.ServiceLocator;
import com.jadventure.game.Game;
import com.jadventure.game.notification.DeathObserver;

/**
 * Called when creating a new Player
 */
public class ChooseClassMenu extends Menus {

    public ChooseClassMenu() {
        this.menuItems.add(new MenuItem("Recruit", "A soldier newly enlisted to guard the city of Silliya"));
        this.menuItems.add(new MenuItem("SewerRat", "A member of the underground of Silliya"));

        while(true) {
            ServiceLocator.getIOHandler().sendOutput("Choose a class to get started with:");
            MenuItem selectedItem = displayMenu(this.menuItems);
            if(testOption(selectedItem)) {
            	break;
            }
        }
    }

    private static boolean testOption(MenuItem m)  {
        String key = m.getKey();
        if(key.equals("recruit")) {
            Player player = Player.getInstance("recruit");
            //register death
            player.addObserver(new DeathObserver());
            new Game(player, "new");
            return true;
        } else if(key.equals("sewerrat")) {
            Player player = Player.getInstance("sewerrat");
            //register death
            player.addObserver(new DeathObserver());

            new Game(player, "new");
            return true;
        } else {
            return false;
        }
    }
}
