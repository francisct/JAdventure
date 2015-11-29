package com.jadventure.game.monsters;

import com.jadventure.game.entities.Entity;
import com.jadventure.game.entities.EquipmentLocation;
import com.jadventure.game.items.Item;
import com.jadventure.game.GameBeans;
import com.jadventure.game.QueueProvider;
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
	private String name;
	private String intro;
	private int level;
	private int strength;
	private int luck;
	protected String weapon = "hands";
	protected Map<EquipmentLocation, Item> equipment;

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

    public String getName() {
	    return this.name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public void setIntro(String intro) {
	    this.intro = intro;
	}

	public String getIntro() {
	    return this.intro;
	}

	public int getLevel() {
	    return level;
	}

	public void setLevel(int level) {
	    this.level = level;
	}

	public Map<EquipmentLocation, Item> getEquipment() {
	    return Collections.unmodifiableMap(equipment);
	}

	public void setEquipment(Map<EquipmentLocation, Item> equipment) {
	    this.equipment = equipment;
	}

	public int getStrength() {
	    return strength;
	}

	public void setStrength(int strength) {
	    this.strength = strength;
	}

	public int getLuck() {
	    return luck;
	}

	public void setLuck(int luck) {
	    this.luck = luck;
	}

	public String getWeapon() {
	    return weapon;
	}
}
