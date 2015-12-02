package com.jadventure.game.entities;

import com.jadventure.game.items.Item;
import com.jadventure.game.items.ItemStack;

public interface IEntity {
	
	public void printStorage();
	    
	public void addItemToStorage(Item item);

    public void removeItemFromStorage(Item item);
}