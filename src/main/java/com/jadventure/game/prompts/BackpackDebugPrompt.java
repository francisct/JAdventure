package com.jadventure.game.prompts;

import com.jadventure.game.entities.Player;
import com.jadventure.game.items.Item;
import com.jadventure.game.repository.RepositoryException;
import com.jadventure.runtime.ServiceLocator;
import com.jadventure.game.repository.ItemRepository;
import com.jadventure.game.GameBeans;



/**
 * BackpackDebugPrompt is for editing the backpack contents
 * during debugging
 *
 * Items are added by their names and removed by their display name
 */
public class BackpackDebugPrompt{
    // @Resource
    protected static ItemRepository itemRepo = GameBeans.getItemRepository();

    private static String helpText = "\nlist: Lists the current item the player has\n"+
                                     "add: Add a new item\n"+
                                     "remove: Remove an item\n"+
                                     "help: Prints this info\n"+
                                     "exit: Exits the BackpackDebugMenu\n";

    public BackpackDebugPrompt(Player player){
        boolean continuePrompt = true;
        while(continuePrompt){
            ServiceLocator.getIOHandler().sendOutput("Edit backpack:");
            String command = ServiceLocator.getIOHandler().getInput();
            continuePrompt = parse(player, command.toLowerCase());
        }
    }
    public static boolean parse(Player player, String command){
        boolean continuePrompt = true;
        
        try {
            if (command.startsWith("add")){
                try {
                    Item appendItem = itemRepo.getItem(command.substring(3).trim());
                    if (appendItem.getName() != null)
                        player.addItemToStorage(appendItem);
                } catch (RepositoryException ex) {
                    ServiceLocator.getIOHandler().sendOutput(ex.getMessage());
                }
            }
            else if (command.startsWith("remove")){
                String removeItemName = command.substring(6).trim();
                player.dropItem(removeItemName);
            }
            else if (command.equals("list")){
                player.printBackPack();
            }
            else if (command.equals("help"))
                ServiceLocator.getIOHandler().sendOutput(helpText);
            else if (command.equals("exit"))
                continuePrompt = false;
        } catch (NumberFormatException e){
            ServiceLocator.getIOHandler().sendOutput("Invalid item name");
        }
        
        return continuePrompt;
    }
}
