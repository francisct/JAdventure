package com.jadventure.game.entities;

import java.util.HashMap;
import java.util.Map;

import com.jadventure.game.QueueProvider;
import com.jadventure.game.items.Item;

public class Human extends Entity{

	public Map<String, String> equipItem(EquipmentLocation place, Item item) {
	    double oldDamage = this.damage;
	    int oldArmour = this.armour;
	    if (place == null) {
	        place = item.getPosition();
	    }
	    if (equipment.get(place) != null) {
	        unequipItem(equipment.get(place));
	    }
	    if (place == EquipmentLocation.BOTH_HANDS) {
	        unequipTwoPlaces(EquipmentLocation.LEFT_HAND, EquipmentLocation.RIGHT_HAND);
	    } else if (place == EquipmentLocation.BOTH_ARMS) {
	        unequipTwoPlaces(EquipmentLocation.LEFT_ARM, EquipmentLocation.RIGHT_ARM);
	    } 
	    Item bothHands = equipment.get(EquipmentLocation.BOTH_HANDS);
	    if (bothHands != null && (EquipmentLocation.LEFT_HAND == place || EquipmentLocation.RIGHT_HAND == place)) { 
	        unequipItem(bothHands);
	    }
	    Item bothArms = equipment.get(EquipmentLocation.BOTH_ARMS);
	    if (bothArms != null && (place == EquipmentLocation.LEFT_ARM || place == EquipmentLocation.RIGHT_ARM)) { 
	        unequipItem(bothArms);
	    }
	    equipment.put(place, item);
	    removeItemFromStorage(item);
	    Map<String, String> result = new HashMap<String, String>();
	    switch (item.getId().charAt(0)) {
	        case 'w': {
	            this.weapon = item.getId();
	            this.damage += item.getProperty("damage");
	            double diffDamage = this.damage - oldDamage;
	            result.put("damage", String.valueOf(diffDamage));
	            break;
	        }
	        case 'a': {
	            this.armour += item.getProperty("armour");
	            int diffArmour = this.armour - oldArmour;
	            result.put("armour", String.valueOf(diffArmour));
	            break;
	        }
	        case 'p': {
	            if (item.containsProperty("healthMax")) {
	                int healthOld = this.getHealth();
	                this.healthMax += item.getProperty("healthMax");
	                this.health += item.getProperty("health");
	                this.health = (this.health > this.healthMax) ? this.healthMax : this.health;
	                int healthNew = this.health;
	                unequipItem(item); // One use only
	                removeItemFromStorage(item);
	                if (healthNew != healthOld) {
	                    result.put("health", String.valueOf(health - healthOld));
	                } else {
	                    result.put("health", String.valueOf(item.getProperty("healthMax")));
	                }
	            }
	            break;
	        }
	        case 'f': {
	            int healthOld = this.getHealth();
	            this.health += item.getProperty("health");
	            this.health = (this.health > this.healthMax) ? this.healthMax
	                    : this.health;
	            unequipItem(item); // One use only
	            removeItemFromStorage(item);
	            result.put("health", String.valueOf(health - healthOld));
	            break;
	        }
	    }
	    return result;
	}

	private void unequipTwoPlaces(EquipmentLocation leftLocation, EquipmentLocation rightLocation) {
	    Item left = equipment.get(leftLocation);
	    Item right = equipment.get(rightLocation);
	    if (left != null) {
	        unequipItem(left);
	    }
	    if (right != null) { 
	        unequipItem(right);
	    }
	}

	public Map<String, String> unequipItem(Item item) {
	    for (EquipmentLocation key : equipment.keySet()) {
	        if (item.equals(equipment.get(key))) {
	            equipment.put(key, null);
	        }
	    }
	    if (!item.equals(itemRepo.getItem("hands"))) {
	        addItemToStorage(item);
	    }
	    Map<String, String> result = new HashMap<String, String>();
	    if (item.containsProperty("damage")) {
	        double oldDamage = damage;
	        weapon = "hands";
	        damage -= item.getProperty("damage");
	        double diffDamage = damage - oldDamage;
	        result.put("damage", String.valueOf(diffDamage));
	    } 
	    if (item.containsProperty("armour")) {
	        int oldArmour = armour;
	        armour -= item.getProperty("armour");
	        int diffArmour = armour - oldArmour;
	        result.put("armour", String.valueOf(diffArmour));
	    }
	    return result;
	}

	public void printEquipment() {
	    QueueProvider.offer("\n------------------------------------------------------------");
	    QueueProvider.offer("Equipped Items:");
	    if (equipment.keySet().size() == 0) {
	        QueueProvider.offer("--Empty--");
	    } else {
	        int i = 0;
	        Item hands = itemRepo.getItem("hands");
	        Map<EquipmentLocation, String> locations = new HashMap<>();
	        locations.put(EquipmentLocation.HEAD, "Head");
	        locations.put(EquipmentLocation.CHEST, "Chest");
	        locations.put(EquipmentLocation.LEFT_ARM, "Left arm");
	        locations.put(EquipmentLocation.LEFT_HAND, "Left hand");
	        locations.put(EquipmentLocation.RIGHT_ARM, "Right arm");
	        locations.put(EquipmentLocation.RIGHT_HAND, "Right hand");
	        locations.put(EquipmentLocation.BOTH_HANDS, "Both hands");
	        locations.put(EquipmentLocation.BOTH_ARMS, "Both arms");
	        locations.put(EquipmentLocation.LEGS, "Legs");
	        locations.put(EquipmentLocation.FEET, "Feet");
	        for (Map.Entry<EquipmentLocation, Item> item : equipment.entrySet()) {
	            if (item.getKey() != null && !hands.equals(item.getValue()) && item.getValue() != null) {
	                QueueProvider.offer(locations.get(item.getKey()) + " - " + item.getValue().getName());
	            } else {
	                i++;
	            }
	        }
	        if (i == equipment.keySet().size()) {
	            QueueProvider.offer("--Empty--");
	        }
	    }
	    QueueProvider.offer("------------------------------------------------------------"); 
	}

}
