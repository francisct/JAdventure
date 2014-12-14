package com.jadventure.game.prompts;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jadventure.game.DeathException;
import com.jadventure.game.QueueProvider;
import com.jadventure.game.entities.Player;
import com.jadventure.game.monsters.Monster;
import com.jadventure.game.monsters.MonsterFactory;
import com.jadventure.game.navigation.Coordinate;
import com.jadventure.game.navigation.Direction;
import com.jadventure.game.navigation.ILocation;
import com.jadventure.game.navigation.LocationManager;
import com.jadventure.game.navigation.LocationType;

/**
 * CommandCollection contains the declaration of the methods mapped to game commands
 *
 * The declared command methods are accessed only by reflection.
 * To declare a new command, add an appropriate method to this class and Annotate it with
 * Command(command, aliases, description)
 */
public enum CommandCollection {
    INSTANCE;

    public Player player;

    private HashMap<String, String> directionLinks = new HashMap<String,String>()
    {{
         put("n", "north");
         put("s", "south");
         put("e", "east");
         put("w", "west");
         put("u", "up");
         put("d", "down");
    }};

    public static CommandCollection getInstance() {
        return INSTANCE;
    }

    public void initPlayer(Player player) {
        this.player = player;
    }

    // command methods here

    @Command(command="help", aliases="h", description="Prints help", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_help() {
        Method[] methods = CommandCollection.class.getMethods();
        int commandWidth = 0;
        int descriptionWidth = 0;
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }
            Command annotation = method.getAnnotation(Command.class);
            String command = annotation.command() + "( " + annotation.aliases() + "):";
            String description = annotation.description();
            if (command.length() > commandWidth) {
                commandWidth = command.length();
            }
            if (description.length() > descriptionWidth) {
                descriptionWidth = description.length();
            }
        }
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }
            Command annotation = method.getAnnotation(Command.class);
            String command = (annotation.aliases().length() == 0) ? 
                annotation.command() : annotation.command() + " (" + annotation.aliases() + "):"; 
            String message = String.format("%-" +commandWidth + "s %-" + descriptionWidth + "s", 
                    command, 
                    annotation.description());
            if (annotation.debug()) {
                if ("test".equals(player.getName())) {
                    QueueProvider.offer(message);
                }
            } else {
                QueueProvider.offer(message);
                
            }
        }
    }

    @Command(command="save", aliases="s", description="Save the game", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_save() {
        player.save();
    }

    @Command(command="monster", aliases="m", description="Monsters around you", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_m() {
        List<Monster> monsterList = player.getLocation().getMonsters();
        if (monsterList.size() > 0) {
            QueueProvider.offer("Monsters around you:");
            QueueProvider.offer("----------------------------");
            for (Monster monster : monsterList) {
                QueueProvider.offer(monster.monsterType);
            }
            QueueProvider.offer("----------------------------");
        } else {
            QueueProvider.offer("There are no monsters around you'n");
        }
    }

    @Command(command="goto", aliases="g", description="Goto a direction", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_g(String arg) throws DeathException {
        ILocation location = player.getLocation();

        try {
            arg = directionLinks.get(arg);
            Direction direction = Direction.valueOf(arg.toUpperCase());
            Map<Direction, ILocation> exits = location.getExits();
            if (exits.containsKey(direction)) {
                ILocation newLocation = exits.get(Direction.valueOf(arg.toUpperCase()));
                if (!newLocation.getLocationType().equals(LocationType.WALL)) {
                    player.setLocation(newLocation);
                    player.getLocation().print();
                    Random random = new Random();
                    if (player.getLocation().getMonsters().size() == 0) {
                        MonsterFactory monsterFactory = new MonsterFactory();
                        int upperBound = random.nextInt(player.getLocation().getDangerRating() + 1);
                        for (int i = 0; i < upperBound; i++) {
                            Monster monster = monsterFactory.generateMonster(player);
                            player.getLocation().addMonster(monster);
                        }
                    }
                    if (random.nextDouble() < 0.5) {
                        List<Monster> monsters = player.getLocation().getMonsters();
                        if (monsters.size() > 0) {
                            int posMonster = random.nextInt(monsters.size());
                            String monster = monsters.get(posMonster).monsterType;
                            QueueProvider.offer("A " + monster + " is attacking you!");
                            player.attack(monster);
                        }
                    }
                } else {
                    QueueProvider.offer("You cannot walk through walls.");
                }
            } else {
                QueueProvider.offer("The is no exit that way.");
            }
        } catch (IllegalArgumentException ex) {
            QueueProvider.offer("That direction doesn't exist");
        } catch (NullPointerException ex) {
            QueueProvider.offer("That direction doesn't exist");
        }
    }

    @Command(command="inspect", aliases="i", description="Inspect an item", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_i(String arg) {
        player.inspectItem(arg.trim());
    }

    @Command(command="equip", aliases="e", description="Equip an item", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_e(String arg) {
        player.equipItem(arg.trim());
    }

    @Command(command="unequip", aliases="ue", description="Unequip an item", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_ue(String arg) {
        player.dequipItem(arg.trim());
    }

    @Command(command="view", aliases="v", description="View details", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_v(String arg) {
        arg = arg.trim();
        switch (arg) {
            case "s":
            case "stats":
                player.getStats();
                break;
            case "e":
            case "equipped":
                player.printEquipment();
                break;
            case "b":
            case "backpack":
                player.printStorage();
                break;
            default:
                QueueProvider.offer("That is not a valid display");
                break;
        }
    }

    @Command(command="pick", aliases="p", description="Pick up an item", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_p(String arg) {
        player.pickUpItem(arg.trim());
    }

    @Command(command="drop", aliases="d", description="Drop an item", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_d(String arg) {
        player.dropItem(arg.trim());
    }

    @Command(command="attack", aliases="a", description="Attacks an entity", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_a(String arg) throws DeathException {
       player.attack(arg.trim());
    }

    @Command(command="lookaround", aliases="la", description="Displays the description of the room you are in.", debug=false)
    @SuppressWarnings("UnusedDeclaration")
    public void command_la() {
       player.getLocation().print(); 
    }

    // Debug methods here

    @Command(command="attack", aliases="", description="Adjusts the damage level the player has", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_attack(String arg) {
        double damage = Double.parseDouble(arg);
        player.setDamage(damage);
    }

    @Command(command="maxhealth", aliases="", description="Adjusts the maximum health of the player", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_maxhealth(String arg) {
        int healthMax = Integer.parseInt(arg);
        if (healthMax > 0) {
            player.setHealthMax(healthMax);
        } else {
            QueueProvider.offer("Maximum health must be possitive");
        }
    }

    @Command(command="health", aliases="", description="Adjusts the amount of gold the player has", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_health(String arg) {
        int health = Integer.parseInt(arg);
        if (health > 0) {
            player.setHealth(health);
        } else {
            QueueProvider.offer("Health must be possitive");
        }
    }

    @Command(command="armour", aliases="", description="Adjusts the amount of armour the player has", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_armour(String arg) {
        int armour = Integer.parseInt(arg);
        player.setArmour(armour);
    }

    @Command(command="level", aliases="", description="Adjusts the level of the player", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_level(String arg) {
        int level = Integer.parseInt(arg);
        player.setLevel(level);
    }

    @Command(command="gold", aliases="", description="Adjusts the amount of gold the player has", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_gold(String arg) {
        int gold = Integer.parseInt(arg);
        player.setGold(gold);
    }

    @Command(command="teleport", aliases="", description="Moves the player to specified coordinates", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_teleport(String arg) {
        ILocation newLocation = LocationManager.getLocation(new Coordinate(arg));
        ILocation oldLocation = player.getLocation();
        try {
            player.setLocation(newLocation);
            player.getLocation().print();
        } catch (NullPointerException e) {
            player.setLocation(oldLocation);
            QueueProvider.offer("There is no such location");
        }
    }

    @Command(command="backpack", aliases="", description="Opens the backpack debug menu.", debug=true)
    @SuppressWarnings("UnusedDeclaration")
    public void command_backpack(String arg) {
        new BackpackDebugPrompt(player);
    }
}
