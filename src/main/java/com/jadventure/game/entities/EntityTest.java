package com.jadventure.game.entities;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jadventure.game.DeathException;
import com.jadventure.game.items.Item;
import com.jadventure.game.items.Storage;
import com.jadventure.game.monsters.Monster;
import com.jadventure.game.monsters.MonsterFactory;
import com.jadventure.game.navigation.Location;
import com.jadventure.game.navigation.LocationType;

public class EntityTest {

	
	
	@Test
	public void testEntity() {
		
		Entity entity = new NPC();
		
		//Verifies that the constructor creates the entity correctly
		entity.setArmour(75);
		assertEquals(entity.getArmour(),75);
		
		assertEquals(entity.getDamage(),30.0,0);
		entity.setDamage(50.0);
		assertEquals(entity.getDamage(),50.0,0);
		
		//Tests that the item can be correctly equipped
		Map m1 = new HashMap();	
		m1.put("armour", 25);
		Item item = new Item("a", "type", "name", "description", 0, m1);
		Map  m2 = ((Human) entity).equipItem(EquipmentLocation.HEAD, item);
		assertEquals(m2.get("armour"),"25");
		
	}
	
	@Test
	public void testNPC(){
		NPC entity = new NPC();
		
		//test properties of the NPC
		entity.setXPGain(20);
		assertEquals(entity.getXPGain(),20);
		
		//Test to see if two instance of NPC are the same
		NPC same1 = new NPC("9000");
		assertEquals(same1.getId(), "9000");
		
		NPC same2 = new NPC("9000");
		assertEquals(same2.getId(),"9000");
		
		NPC notSame = new NPC("9001");
		
		assert(same1.equals(same2));
		assertEquals(same1.equals(notSame),false);
	}
	
	@Test
	public void testPlayer(){
		Player player = new Player();
		
		player.setCharacterLevel("Mage", 10);
		assertEquals(player.getCharacterLevel("Mage"),10);
		
		Map m1 = new HashMap();	
		m1.put("damage", 15);
		Item item = new Item("w", "type", "name", "description", 0, m1);
		Map  m2 = player.equipItem(EquipmentLocation.RIGHT_ARM, item);
		assertEquals(m2.get("damage"),"15.0");
		
		Location loc = new Location();
		loc.setLocationType(LocationType.CAVE);
		player.setLocation(loc);
		
		
		
		//add test for when player is attacking
		try {
			player.attack("goblin");
		} catch (DeathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	
	

}
