package com.jadventure.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadventure.game.entities.Entity;
import com.jadventure.game.entities.NPC;
import com.jadventure.game.entities.Player;
import com.jadventure.game.items.Item;
import com.jadventure.game.menus.MenuItem;
import com.jadventure.game.menus.Menus;
import com.jadventure.game.repository.ItemRepository;
import com.jadventure.runtime.ServiceLocator;

public class Trading {
    NPC npc;
    Player player;
    ItemRepository itemRepo = GameBeans.getItemRepository();

    public Trading(NPC npc, Player player) {
        this.npc = npc;
        this.player = player;
    }

    public void trade(boolean buy, boolean sell) {
        List<MenuItem> tradeList = new ArrayList<>();
        String buyCommand = "Buy from " + npc.getName();
        String sellCommand = "Sell to " + npc.getName();
        if (buy) {
            tradeList.add(new MenuItem(buyCommand, null));
        }
        if (sell) {
            tradeList.add(new MenuItem(sellCommand, null));
        }
        tradeList.add(new MenuItem("Exit", null));
        Menus tradeMenu = new Menus();
        MenuItem response = tradeMenu.displayMenu(tradeList);
        String command = response.getCommand();
        if (command.equals(buyCommand) && buy) {
            playerBuy();
        } else if (command.equals(sellCommand) && sell) {
            playerSell();
        } else if (command.equals("Exit")) {
            return;
        }
        trade(buy, sell);
    }
    
    public void playerBuy() {
        ServiceLocator.getIOHandler().sendOutput(npc.getName() + "'s items:\t" + npc.getName()  + "'s gold:" + npc.getGold() + "\n");
        ServiceLocator.getIOHandler().sendOutput(npc.getStorage().displayWithValue());

        ServiceLocator.getIOHandler().sendOutput("You have " + player.getGold() + " gold coins.\nWhat do you want to buy?");
        String itemName = ServiceLocator.getIOHandler().getInput();

        if ("exit".equals(itemName) || "back".equals(itemName)) {
            return;
        }

        Item item = tradeItem(npc, player, itemName);
        if (item != null) {
            if (item != itemRepo.getItem("empty")) {
                ServiceLocator.getIOHandler().sendOutput("You have bought a " + item.getName() + " for " + item.getProperties().get("value") + " gold coins.");
                ServiceLocator.getIOHandler().sendOutput("You now have " + player.getGold() + " gold coins remaining.");
            }
            else {
                ServiceLocator.getIOHandler().sendOutput("You do not have enough money!");
            }
        } else {
            ServiceLocator.getIOHandler().sendOutput("Either this item doesn't exist or this character does not own that item");
        }
    }

    public void playerSell() {
        ServiceLocator.getIOHandler().sendOutput(player.getName() + "'s items:\t" + npc.getName()  + "'s gold:" + npc.getGold() + "\n");
        ServiceLocator.getIOHandler().sendOutput(player.getStorage().displayWithValue());
        
        ServiceLocator.getIOHandler().sendOutput("You have " + player.getGold() + " gold coins.\nWhat do you want to sell?");
        String itemName = ServiceLocator.getIOHandler().getInput();
 
        if ("exit".equals(itemName) || "back".equals(itemName)) {
            return;
        }
        Item item = tradeItem(player, npc, itemName);
        if (item != null) {
            if (item != itemRepo.getItem("empty")) {
                ServiceLocator.getIOHandler().sendOutput("You have sold a " + item.getName() + " for " + item.getProperties().get("value") + " gold coins.");
                ServiceLocator.getIOHandler().sendOutput("You now have " + player.getGold() + " gold coins remaining.");
            }
            else {
                ServiceLocator.getIOHandler().sendOutput(npc.getName() + " does not have enough money!");
            }
        } else {
            ServiceLocator.getIOHandler().sendOutput("Either this item doesn't exist or this character does not own that item");
        }
    }

    private Item tradeItem(Entity seller, Entity buyer, String itemName) {
        List<Item> itemList = seller.getStorage().getItems();
        Map<String, String> itemIds = new HashMap<>();
        Map<String, Integer> itemValues = new HashMap<>();
        Map<String, Item> itemIdtoItem = new HashMap<>();

        for (Item item : itemList) {
            String name = item.getName();
            String id = item.getId();
            int value = item.getProperties().get("value");
            itemIds.put(name, id);
            itemValues.put(id, value);
            itemIdtoItem.put(id, item);
        }

        if (itemIds.containsKey(itemName)) {
            int itemValue = itemValues.get(itemIds.get(itemName));
            Item item = itemIdtoItem.get(itemIds.get(itemName));
            if (buyer.getGold() < itemValue) {
                return itemRepo.getItem("empty");
            }
            buyer.addItemToStorage(item);
            buyer.setGold(buyer.getGold() - itemValue);
            seller.setGold(seller.getGold() + itemValue);
            seller.removeItemFromStorage(item);
            return item;
        } else {
            return null;
        }
    }
}
