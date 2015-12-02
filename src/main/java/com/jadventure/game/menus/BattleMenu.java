package com.jadventure.game.menus;

import com.jadventure.game.entities.Entity;
import com.jadventure.game.entities.Player;
import com.jadventure.game.entities.NPC;
import com.jadventure.game.monsters.Monster;
import com.jadventure.runtime.ServiceLocator;

import com.jadventure.game.CharacterChange;
import com.jadventure.game.items.ItemStack;
import com.jadventure.game.items.Item;
import com.jadventure.game.GameBeans;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class BattleMenu extends Menus {

    private Monster monsterOpponent;
    private NPC npcOpponent;
    private Player player;
    private Random random;
    private int armour;
    private double damage;
    private boolean escapeSuccessful = false;

    public BattleMenu(NPC npcOpponent, Player player)  {
        this.random = new Random();
        this.npcOpponent = npcOpponent;
        this.player = player;
        this.menuItems.add(new MenuItem("Attack", "Attack " + npcOpponent.getName() + "."));
        this.menuItems.add(new MenuItem("Defend", "Defend against " + npcOpponent.getName() + "'s attack."));
        this.menuItems.add(new MenuItem("Escape", "Try and escape from " + npcOpponent.getName()));
        this.menuItems.add(new MenuItem("Equip", "Equip an item"));
        this.menuItems.add(new MenuItem("Unequip", "Unequip an item"));
        this.menuItems.add(new MenuItem("View", "View details about your character"));
        this.armour = player.getArmour();
        this.damage = player.getDamage();
        while (npcOpponent.getHealth() > 0 && player.getHealth() > 0 && !escapeSuccessful) {
            ServiceLocator.getIOHandler().sendOutput("\nWhat is your choice?");
            MenuItem selectedItem = displayMenu(this.menuItems);
            testSelected(selectedItem);
        }
        if (player.getHealth() == 0) {
            //player died
            player.stateChanged();
            
        }  else if (npcOpponent.getHealth() == 0) {
            int xp = npcOpponent.getXPGain();
            this.player.setXP(this.player.getXP() + xp);
            int oldLevel = this.player.getLevel();
            int newLevel = (int) (0.075 * Math.sqrt(this.player.getXP()) + 1);
            this.player.setLevel(newLevel);

            // Iterates over the npc's items and if there are any, drops them. 
            // There are two loops due to a ConcurrentModification Exception that occurs
            // if you try to remove the item while looping through the npc's items.
            List<ItemStack> itemStacks = npcOpponent.getStorage().getItemStack();
            List<String> itemIds = new ArrayList<>();
            for (ItemStack itemStack : itemStacks) {
                String itemId = itemStack.getItem().getId();
                itemIds.add(itemId);
            }
            for (String itemId : itemIds) {
                Item item = GameBeans.getItemRepository().getItem(itemId);
                npcOpponent.removeItemFromStorage(item);
                this.player.getLocation().addItem(item);
                ServiceLocator.getIOHandler().sendOutput("Your opponent dropped a " + item.getName());
            }

            this.player.getLocation().removeNpc(npcOpponent);
            this.player.setGold(this.player.getGold() + npcOpponent.getGold());
            ServiceLocator.getIOHandler().sendOutput("You killed a " + npcOpponent.getName() + "\nYou have gained " + xp + " XP and " + npcOpponent.getGold() + " gold");
            if (oldLevel < newLevel) {
                ServiceLocator.getIOHandler().sendOutput("You've are now level " + newLevel + "!");
            }
            CharacterChange cc = new CharacterChange();
            cc.trigger(this.player, "kill", npcOpponent.getName());
        }
    }

    public BattleMenu(Monster monsterOpponent, Player player)  {
        this.random = new Random();
        this.monsterOpponent = monsterOpponent;
        this.player = player;
        this.menuItems.add(new MenuItem("Attack", "Attack " + monsterOpponent.getName() + "."));
        this.menuItems.add(new MenuItem("Defend", "Defend against " + monsterOpponent.getName() + "'s attack."));
        this.menuItems.add(new MenuItem("Escape", "Try and escape from " + monsterOpponent.getName()));
        this.menuItems.add(new MenuItem("Equip", "Equip an item"));
        this.menuItems.add(new MenuItem("Unequip", "Unequip an item"));
        this.menuItems.add(new MenuItem("View", "View details about your character"));
        this.armour = player.getArmour();
        this.damage = player.getDamage();
        while (monsterOpponent.getHealth() > 0 && player.getHealth() > 0 && !escapeSuccessful) {
            ServiceLocator.getIOHandler().sendOutput("\nWhat is your choice?");
            MenuItem selectedItem = displayMenu(this.menuItems);
            testSelected(selectedItem);
        }
        if (player.getHealth() == 0) {
        //player died
            player.stateChanged();
            
        }  else if (monsterOpponent.getHealth() == 0) {
            int xp = monsterOpponent.getXPGain();
            this.player.setXP(this.player.getXP() + xp);
            int oldLevel = this.player.getLevel();
            int newLevel = (int) (0.075 * Math.sqrt(this.player.getXP()) + 1);
            this.player.setLevel(newLevel);

            // Iterates over monster's items and if there are any, drops them. 
            // There are two loops due to a ConcurrentModification Exception that occurs
            // if you try to remove the item while looping through the monster's items.
            List<ItemStack> itemStacks = monsterOpponent.getStorage().getItemStack();
            List<String> itemIds = new ArrayList<>();
            for (ItemStack itemStack : itemStacks) {
                String itemId = itemStack.getItem().getId();
                itemIds.add(itemId);
            }
            for (String itemId : itemIds) {
                Item item = GameBeans.getItemRepository().getItem(itemId);
                monsterOpponent.removeItemFromStorage(item);
                this.player.getLocation().addItem(item);
                ServiceLocator.getIOHandler().sendOutput("Your opponent dropped a " + item.getName());
            }

            this.player.getLocation().removeMonster(monsterOpponent);
            this.player.setGold(this.player.getGold() + monsterOpponent.getGold());
            ServiceLocator.getIOHandler().sendOutput("You killed a " + monsterOpponent.getName() + "\nYou have gained " + xp + " XP and " + monsterOpponent.getGold() + " gold");
            if (oldLevel < newLevel) {
                ServiceLocator.getIOHandler().sendOutput("You've are now level " + newLevel + "!");
            }
            CharacterChange cc = new CharacterChange();
            cc.trigger(this.player, "kill", monsterOpponent.getName());
        }
    }

    private void testSelected(MenuItem m) {
        switch (m.getKey()) {
            case "attack": {
                mutateStats(1, 0.5);
                if (npcOpponent == null) {
                    attack(player, monsterOpponent);
                    attack(monsterOpponent, player);
                } else {
                    attack(player, npcOpponent);
                    attack(npcOpponent, player);
                }
                resetStats();
                break;
            }
            case "defend": {
                mutateStats(0.5, 1);
                if (npcOpponent == null) {
                    ServiceLocator.getIOHandler().sendOutput("\nYou get ready to defend against the " + monsterOpponent.getName() + ".");
                    attack(player, monsterOpponent);
                    attack(monsterOpponent, player);
                } else {
                    ServiceLocator.getIOHandler().sendOutput("\nYou get ready to defend against the " + npcOpponent.getName() + ".");
                    attack(player, npcOpponent);
                    attack(npcOpponent, player);
                }
                resetStats();
                break;
            }
            case "escape": {
                if (npcOpponent == null) {
                    escapeSuccessful = escapeAttempt(player, monsterOpponent);
                } else {
                    escapeSuccessful = escapeAttempt(player, npcOpponent);
                }
                break;
            }
            case "equip": {
                equip();
                break;
            }
            case "unequip": {
                unequip();
                break;
            }
            case "view": {
                viewStats();
                break;
            }
            default: {
                break;
            }
        }
    }

    private boolean escapeAttempt(Player player, Entity attacker) {
        double playerEscapeLevel = player.getIntelligence() + player.getStealth() + player.getDexterity();
        double attackerEscapeLevel = attacker.getIntelligence() + attacker.getStealth() + attacker.getDexterity() + 
            (attacker.getDamage() / playerEscapeLevel);
        double escapeLevel = playerEscapeLevel / attackerEscapeLevel;

        Random rand = new Random();
        int rawLuck = rand.nextInt(player.getLuck()*2) + 1;
        int lowerBound = 60 - rawLuck;
        int upperBound = 80 - rawLuck;
        double minEscapeLevel = (rand.nextInt((upperBound - lowerBound) + 1) + lowerBound) / 100.0;
      
        if (escapeLevel > minEscapeLevel) {
            ServiceLocator.getIOHandler().sendOutput("You have managed to escape the: " + attacker.getName());
            return true;
        } else {
            ServiceLocator.getIOHandler().sendOutput("You failed to escape the: " + attacker.getName());
            return false;
        }
    }

    private void attack(Entity attacker, Entity defender) {
        if (attacker.getHealth() == 0) {
            return;
        }
        double damage = attacker.getDamage();
        double critCalc = random.nextDouble();
        if (critCalc < attacker.getCritChance()) {
            damage += damage;
            ServiceLocator.getIOHandler().sendOutput("Crit hit! Damage has been doubled!");
        }
        int healthReduction = (int) ((((3 * attacker.getLevel() / 50 + 2) * damage * damage / (defender.getArmour() + 1)/ 100) + 2) * (random.nextDouble() + 1));
        defender.setHealth((defender.getHealth() - healthReduction));
        if (defender.getHealth() < 0) {
            defender.setHealth(0);
        }
        ServiceLocator.getIOHandler().sendOutput(healthReduction + " damage dealt!");
        if (attacker instanceof Player) {
            ServiceLocator.getIOHandler().sendOutput("The " + defender.getName() + "'s health is " + defender.getHealth());
        } else {
            ServiceLocator.getIOHandler().sendOutput("Your health is " + defender.getHealth());
        }
    }

    private void mutateStats(double damageMult, double armourMult) {
        armour = player.getArmour();
        damage = player.getDamage();
        player.setArmour((int) (armour * armourMult));
        player.setDamage(damage * damageMult);
    }

    private void resetStats() {
        player.setArmour(armour);
        player.setDamage(damage);
    }

    private void equip() {
        player.printStorage();
        ServiceLocator.getIOHandler().sendOutput("What item do you want to use?");
        String itemName = ServiceLocator.getIOHandler().getInput();
        if (!itemName.equalsIgnoreCase("back")) {
            player.equipItem(itemName);
        }
    }

    private void unequip() {
        player.printEquipment();
        ServiceLocator.getIOHandler().sendOutput("What item do you want to unequip?");
        String itemName = ServiceLocator.getIOHandler().getInput();
        if (!itemName.equalsIgnoreCase("back")) {
            player.dequipItem(itemName);
        }
    }

    private void viewStats() {
        ServiceLocator.getIOHandler().sendOutput("\nWhat is your command? ex. View stats(vs), View Backpack(vb), View Equipment(ve) ");
        String input = ServiceLocator.getIOHandler().getInput();
        switch (input) {
            case "vs":
            case "viewstats":
                player.getStats();
                break;
            case "ve":
            case "viewequipped":
                player.printEquipment();
                break;
            case "vb":
            case "viewbackpack":
                player.printStorage();
                break;
            case "back":
            case "exit":
                break;
            default:
                viewStats();
                break;
        }
    }
}

