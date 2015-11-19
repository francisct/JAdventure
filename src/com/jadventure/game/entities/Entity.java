package com.jadventure.game.entities;

import java.util.HashMap;
import java.util.Map;

import com.jadventure.game.GameBeans;
import com.jadventure.game.items.Item;
import com.jadventure.game.items.ItemStack;
import com.jadventure.game.items.Storage;
import com.jadventure.game.repository.ItemRepository;

/**
 * superclass for all entities (includes player, monsters...)
 */
public abstract class Entity implements IEntity{
    // @Resource
    protected ItemRepository itemRepo = GameBeans.getItemRepository();
    
    // All entities can attack, have health, have names
    protected int healthMax;
    protected int health;
    private int intelligence;
    private int dexterity;
    private int stealth;
    private int gold;
    private String name;
	private int level;
    protected double damage = 30;
    private double critChance = 0.0;
    protected int armour;
    protected Storage storage;

    public Entity() {
    	this(100, 100, "Default", 0, null);
    	//this(100, 100, "default", 0, null, new HashMap<EquipmentLocation, Item>());
    }
    
    public Entity(int healthMax, int health, String name, int gold, Storage storage) {
        this.healthMax = healthMax;
        this.health = health;
        this.gold = gold;
        if (storage != null) {
        	this.storage = storage;
        }
        else {
        	this.storage = new Storage(300);
        }
        this.name = name;
    }
    

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > healthMax) {
            health = healthMax;
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getCritChance() {
        return critChance;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public void setHealthMax(int healthMax) {
        this.healthMax = healthMax;
        if (health > healthMax) {
            health = healthMax;
        }
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }
 
    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getStealth() {
        return stealth;
    }

    public void setStealth(int stealth) {
        this.stealth = stealth;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
    
	public String getName() {
	    return this.name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public int getLevel() {
	    return level;
	}

	public void setLevel(int level) {
	    this.level = level;
	}
	
    public void printStorage() {
       storage.display();
    } 
    
    public void addItemToStorage(Item item) {
        storage.addItem(new ItemStack(1, item));
    }

    public void removeItemFromStorage(Item item) {
        storage.removeItem(new ItemStack(1, item)); 
    }

}
