package com.jadventure.game.monsters;

import com.jadventure.game.entities.Entity;
import com.jadventure.game.entities.EquipmentLocation;
import com.jadventure.game.items.Item;
import com.jadventure.game.GameBeans;
import com.jadventure.game.repository.ItemRepository;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/*
 * This class just holds a type of monster that is 
 * further outlined in its respective file. For now it
 * just holds the monster's name.
 */
public abstract class Monster extends Entity {
    public String monsterType;
    private int xpGain;
    private ItemRepository itemRepo = GameBeans.getItemRepository();

    public int getXPGain() {
        return xpGain;
    }

    public void setXPGain(int xpGain) {
        this.xpGain = xpGain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Monster) {
            Monster m = (Monster) obj;
            return m.monsterType.equals(this.monsterType);
        }
        return false;
    }

    public void addRandomItems(int playerLevel, String... children) {
        List<String> itemList = Arrays.asList(children);
        Random rand = new Random();

        int numItems = 1;
        int i = 0;
        while (i != numItems) {
            for (String itemName : itemList) {
                if (i == numItems) {
                    break;
                }

                int j = rand.nextInt(5) + 1;
                if (j == 1) {
                    Item item = itemRepo.getItem(itemName);
                    addItemToStorage(item);
                    i++;
                }
            }
        }
    }

}
