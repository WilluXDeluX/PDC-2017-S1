/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * this is the main class that sets up the game and calls all the other
 * classes
 * 
 **/
package Game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *this is the main class that sets up the game and calls all the other
 * classes
 * @author William & Callum
 */
public class GameCUI {
    private static final ArrayList<ArrayList<Boolean>> DEFAULTMONSTERS = new ArrayList<>();
    private static final Die D20 = new Die(20); 
    private static final Die D8 = new Die(8);
    private static final Die D6 = new Die(6);
    private static final Die D4 = new Die(4);
    private static final Scanner SCAN = new Scanner(System.in);
    private static final File saveFile= new File("src\\Game\\player_save.txt");
    private static ArrayList<ArrayList<Integer>> roomLayout = new ArrayList<>(); 
    private static ArrayList<ArrayList<String>> description = new ArrayList<>();
    private static ArrayList<ArrayList<Boolean>> monsters = new ArrayList<>();
    private static int totalRoomNumber;
    private static Player player;
    private static int playerPosition = 0;
    private static ArrayList<Monster> currentMonsters = new ArrayList<>();
    private static boolean playerAlive = true;
    private static boolean gameActive = true;
    
    
    /**
     * this is the main method that calls the loading , addMonsters , initailisePlayer
     * , startDungen , EnterDungeon , storyEnd methods it also checks to see if 
     * the player is still alive 
     * @autor William & Callum  
     * @param args 
     */
    public static void main(String[] args) {
        while (gameActive)
        {
            try
            {
                loading();
            }
            catch (IOException e)
            {
                System.out.println("bummer");
            }
           
            boolean correct = false;
            while(!correct)
            {
                if (saveFile.exists())
                {
                    System.out.println("Would you like to load your save? (yes/no)");
                    String input = SCAN.nextLine();
                    if (input.equalsIgnoreCase("yes"))
                    {
                        if(!loadSaveGame())
                        {
                            addMonsters();
                            initialisePlayer();
                        }
                        correct = true;
                        
                    }
                    else if (input.equalsIgnoreCase("no"))
                    {
                        addMonsters();
                        initialisePlayer();
                        correct = true;
                    }
                    else
                    {
                        System.out.println("That is not a valid input!");
                    }
                }
            }
            
    //        System.out.println(player.getName()+"\n"+Arrays.toString(player.getStats()));

            startDungeon();
            enterDungen();
            storyEnd();

            if(!playerAlive)
            {
                System.out.println("You have failed to live through this and was forever");
                System.out.println("forgotten, the dungeon awaits another adventurer to ");
                System.out.println("seek out its secrets. Are you the next adventurer? (yes/no)");
                boolean notSelected = true;
                while (notSelected)
                {
                    String input = SCAN.nextLine();
                    if(input.toUpperCase().equals("YES"))
                    {
                        currentMonsters.clear();
                        playerAlive = true;
                        notSelected = false;
                    }
                    else if(input.toUpperCase().equals("no"))
                    {
                        System.out.println("It seems that you have no interest left here,");
                        System.out.println("Fare well, may you come back again for more");
                        gameActive = false;
                        notSelected = false;
                    }
                    else
                    {
                        System.out.println("What was that? I didn't quite catch that");
                    }
                }

            }
        }
        

        
    }
    
    
    /**
     * this method takes in the attacker and defender and rolls a die to find out if
     * they hit or not 
     * @param attacker
     * @param defender
     * @return whether the unit hits or misses
     */
    public static boolean d20Check(Unit attacker, Unit defender)
    {
        boolean hit = false;
        int hitRating = attacker.getAttack()+D20.newRoll();
        int armor = defender.getArmor();
        
        if (hitRating >= armor)
        {
            hit = true;
        }
        else
        {
            hit = false;
        }
        return hit;
    }
    
    
    /**
     * this method takes in the attacker and defender information
     * and deals damage baced on the units information 
     * also prints out the damage you did or if you missed your attack
     * @param attacker
     * @param defender 
     */
    public static void attack(Unit attacker, Unit defender)
    {
        int damage = 0;
        int damage1 = 0;
        int damage2 = 0;
        boolean isHit = d20Check(attacker, defender);
        if (isHit)
        {
            if (attacker.getType() == 0)
            {
                System.out.print("You attacked and hit the "+defender.getName());
            }
            else
            {
                System.out.print("The " + attacker.getName() + " hits you ("+defender.getName()+")");
            }
            switch(attacker.getDamageClass())
            {
                case 4:
                    damage = d4Damage(defender);
                    break;
                case 6:
                    damage = d6Damage(defender);
                    break;
                case 8:
                    damage = d8Damage(defender);
                    break;
                case 5:
                    damage1 = d4Damage(defender);
                    damage2 = d4Damage(defender);
                    break;
            }
            switch(attacker.getDamageClass())
            {
                case 4:
                case 6:
                case 8:
                    if (defender.getType() == 0)
                    {
                        System.out.println(" for " + damage + " damage, you have "+player.getHp()+"HP left");
                    }
                    else
                    {
                        System.out.println(" for " + damage + " damage");
                    }
                    break;
                case 5:
                    System.out.println(" for " + damage1 + " and " + damage2 +  " damage");
                    break;
            }
            
        }
        else
        {
            if (attacker.getType() == 0)
            {
                System.out.println("You missed your attack!!");
            }
            else
            {
                System.out.println(attacker.getName()+" missed the attack on you");
            }
        }
    }
    
    
    /**
     * this method sets up a new die and takes the damage off the defender
     * @param defender
     * @return damage
     */
    public static int d8Damage(Unit defender)
    {
        int damage = D8.newRoll();
        int currentHP = defender.getHp();
        defender.setHp(currentHP - damage);
        return damage;
    }
    
     /**
     * this method sets up a new die and takes the damage off the defender
     * @param defender
     * @return damage
     */
    public static int d6Damage(Unit defender)
    {
        int damage = D6.newRoll();
        int currentHP = defender.getHp();
        defender.setHp(currentHP - damage);
        return damage;
    }
     /**
     * this method sets up a new die and takes the damage off the defender
     * @param defender
     * @return damage
     */
    
    public static int d4Damage(Unit defender)
    {
        int damage = D4.newRoll();
        int currentHP = defender.getHp();
        defender.setHp(currentHP - damage);
        return damage;
    }
    
    
    /**
     * this method sets up the player and asks what the player would like 
     * @auther William
     */
    public static void initialisePlayer()
    {
        System.out.print("Please enter your name: ");
        String name = SCAN.nextLine();
        System.out.println("Please select your class, use the respective number:");
        boolean noClassSelected = true;
        while (noClassSelected)
        {
            boolean isIncorrect = true;
            while (isIncorrect)
            {
                
                    System.out.println("1: Barbarian");
                    System.out.println("2: Cleric");
                    System.out.println("3: Ranger");
                    System.out.println("4: Mage");
                    try
                    {
                        int input = SCAN.nextInt();
                        switch(input)
                        {
                            case 1:
                                player = new Player(name, PlayerClass.barb());
                                player.setDamageClass(8);
                                isIncorrect = false;
                                noClassSelected = false;
                                break;
                            case 2:
                                player = new Player(name, PlayerClass.cleric());
                                player.setDamageClass(6);
                                isIncorrect = false;
                                noClassSelected = false;
                                break;
                            case 3:
                                player = new Player(name, PlayerClass.ranger());
                                player.setDamageClass(8);
                                isIncorrect = false;
                                noClassSelected = false;
                                break;
                            case 4:
                                player = new Player(name, PlayerClass.mage());
                                player.setDamageClass(5);
                                isIncorrect = false;
                                noClassSelected = false;
                                break;
                            default: 
                                System.out.println("Invalid selection, try again.");
                                break;
                        }
                    }
                    catch(InputMismatchException e)
                    {
                        System.out.println("Thats not a number, try again");
                    }
                    SCAN.nextLine();
                    
                
            }
        }
    }
    
    public static boolean loadSaveGame()
    {
        boolean loadSuccessful = false;
        boolean haveFile = false;
        while(!haveFile)
        {
            try {
                BufferedReader buffer = new BufferedReader(new FileReader(saveFile));
                String name = buffer.readLine();
                String statsArray = buffer.readLine();
                int[] stats;
//                ArrayList<ArrayList<Boolean>> monsters = new ArrayList<>();
                stats = Arrays.stream(statsArray.substring(1, statsArray.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();//credit to Saul on stackoverflow http://stackoverflow.com/questions/7646392/convert-string-to-int-array-in-java
                player = new Player(name, stats);
                player.setDamageClass(Integer.parseInt(buffer.readLine()));
                playerPosition = Integer.parseInt(buffer.readLine());
                for (int i = 0; i < totalRoomNumber; i++)
                {
                    String[] monsterString = buffer.readLine().replace("[","").replace("]","").split(",\\s");
                    Boolean[] monsterArray = Arrays.stream(monsterString).map(Boolean::parseBoolean).toArray(Boolean[]::new);//credit to ΦXocę 웃 Пepeúpa ツ on stackoverflow http://stackoverflow.com/questions/39873596/convert-array-of-strings-to-boolean-list-in-java
                    ArrayList<Boolean> monsterList = new ArrayList<>(Arrays.asList(monsterArray));
                    monsters.add(monsterList);
                }
                haveFile = true;
                loadSuccessful = true;
            } 
            catch (FileNotFoundException ex) {
                System.out.println("File was not loaded! A new game has been started for you.");
            }
            catch (IOException e)
            {
                System.out.println("File was not loaded! A new game has been started for you.");
                haveFile = true;
            }
        }
        return loadSuccessful;
    }
    
    public static void saveGame()
    {
        player.updateStats();
        try {
            BufferedWriter writer;
            writer = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(saveFile)));
            
            writer.write(player.getName()+"\n"+Arrays.toString(player.getStats())+"\n"+player.getDamageClass()+"\n"+playerPosition);
            for (int i = 0; i < monsters.size(); i++)
            {
                writer.write("\n"+monsters.get(i));
            }
            
            writer.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("NOOOO");
        } 
        catch (IOException ex) {
            System.out.println("IOEx!!");
        }
    }
    
    public static void loading() throws IOException
    {
        /**
         * this method takes in the items from the file 
         * and loads them in to the corresponding arrays
         * @param the_drangons_dungeon.txt
         * @author Callum
         */
        
        String[] connectedRooms ;
        String load;

        Scanner fileScan = new Scanner(new File("src\\Game\\the_dragons_dungeon.txt"));

        System.out.println("Loading Game");
        
        String roomNumber = fileScan.nextLine();
        totalRoomNumber = Integer.valueOf(roomNumber);
        fileScan.nextLine();
        
        int roomCounter = 0;
        while (fileScan.hasNext())
        {
            load = fileScan.nextLine();
            if (load.toUpperCase().startsWith("@DESCRIPTION"))
            {
//               
                load = fileScan.nextLine();
                description.add(new ArrayList<>());
                while ((!(load.toUpperCase().startsWith("@CONNECT")) && fileScan.hasNext()))
                {
                    description.get(roomCounter).add(load);
                    load = fileScan.nextLine();
                }
            }
            if (load.toUpperCase().startsWith("@CONNECT"))
            {
//                
                roomLayout.add(new ArrayList<>());
                load = fileScan.nextLine();
                connectedRooms = load.split(",");
                for (String room :connectedRooms)
                {
                    roomLayout.get(roomCounter).add(Integer.parseInt(room));
                }
//
//               
            }
//           
//            }
            if(load.toUpperCase().startsWith("+"))
            {
                ++roomCounter;
                
            }
        }
        
        fileScan.close();

    }
    
    public static void addMonsters()
    {
        /**
         * this method adds monsters to the array at the approritae possition
         */
        for (int i = 0; i < totalRoomNumber; i++)
        {
            DEFAULTMONSTERS.add(new ArrayList<>());
            for (int j = 0; j < 4; j++)
            {
                if ((i == 4 || i == 12)&& (j == 0 || j == 1))
                {
                    DEFAULTMONSTERS.get(i).add(true);
                }
                else if ((i == 6 || i == 10 || i == 12) & j == 3)
                {
                    DEFAULTMONSTERS.get(i).add(true);
                }
                else if ((i == 8|| i == 12) && j == 2)
                {
                    DEFAULTMONSTERS.get(i).add(true);
                }
                else
                {
                    DEFAULTMONSTERS.get(i).add(false);
                }
            }
        }
        monsters = DEFAULTMONSTERS;
    }
    
    public static boolean haveMonster()
    {
        /**
         * this method is to see if there is a monster in the room 
         */
        boolean haveMonster;
        haveMonster = monsters.get(playerPosition).contains(true);
        return haveMonster;
    }
    
    public static void setMonster()
    {
        /**
         * in this method sets the apropriate monster in the room 
         * @auther William
         */
        ArrayList<Monster> roomMonsters = new ArrayList<>();
        for (int i = 0; i < 4; i ++)
        {
            boolean haveMonster = monsters.get(playerPosition).get(i);
            if (haveMonster)
            {
                switch (i)
                {
                    case 0:
                        roomMonsters.add(new Monster("kobold",MonsterClass.kobold(),1,4));
                        break;
                    case 1:
                        roomMonsters.add(new Monster("goblin",MonsterClass.goblin(),2,4));
                        break;
                    case 2:
                        roomMonsters.add(new Monster("gnoll",MonsterClass.gnoll(),3,6));
                        break;
                    case 3:
                        roomMonsters.add(new Monster("orc",MonsterClass.orc(),4,8));
                        break;
                }
            }
        }
        currentMonsters = roomMonsters;
    }

    public static void removeMonster(int index)
            
    {
        /**
         * this method removes monster from the room when you defeat them 
         * @auther William
         */
        int monsterType = currentMonsters.get(index).getType();
        switch(monsterType)    
        {
            case 1:
                monsters.get(playerPosition).set(monsterType-1, false);
                break;
            case 2:
                monsters.get(playerPosition).set(monsterType-1, false);
                break;
            case 3:
                monsters.get(playerPosition).set(monsterType-1, false);
                break;
            case 4:
                monsters.get(playerPosition).set(monsterType-1, false);
                break;
                    
        }
        currentMonsters.remove(currentMonsters.get(index));
    }
    
    
    
    public static boolean isMonsterAlive (Monster monster)
    {
        /**
         * this method is a simple is the monster alive or not 
         * @return if its alive or not 
         * @auther William
         */
        boolean isAlive;
        if(monster.getHp()<= 0)
        {
            isAlive = false;
        }
        else
        {
            isAlive = true;
        }
        
        return isAlive;
    }
    
    public static void printTargets()
    {
        /**
         * this method who you will attack and sets a number to it 
         * @auther WIlliam
         * 
         */
        System.out.println("Who will you attack?");
        int index = 1;
        for(Monster monster : currentMonsters)
        {
            System.out.println((index++)+": "+monster.getName());
        }
    }
    
    public static void inCombat()
    {
        int input;
        boolean isIncorrect = true;
        while (isIncorrect)
        {
            try 
            {
                input = Integer.parseInt(SCAN.nextLine());
                if(input > 0 && input <= currentMonsters.size())
                {
                    attack(player, currentMonsters.get(input-1));
                    isIncorrect = false;
                }
                else
                {
                    System.out.println("That is not a valid number.");
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("That is an invalid target.(Enter the number!)");
            }
        }
        for (int i = 0;i < currentMonsters.size();i++)
        {
            if (!(isMonsterAlive(currentMonsters.get(i))))
            {
                System.out.println("You have killed the "+currentMonsters.get(i).getName() + "!");
                removeMonster(i);
            }
        }
        for (int i = 0; i < currentMonsters.size(); i++)
        {
            attack(currentMonsters.get(i), player);
            isPlayerAlive();
            if (!playerAlive)
            {
                break;
            }
        }
        
    }
    
    public static void isPlayerAlive()
    {
        if(player.getHp() <= 0)
        {
            playerAlive = false;
        }
    }
    
    public static void printDescription()
    {
        ArrayList<String> currentDescription = description.get(playerPosition);
//        int index = 0;
        for (String d: currentDescription)
        {
            System.out.println(d);
//            index++;
        }
    }
    
    public static void startDungeon()
    {
        
        
        /**
         * this method sets up the start of the dungeon so that the player 
         * can only move farward 
         * @auther Callum
         */
        
        while(playerPosition <= 3)
        {
            printDescription();
            String input = SCAN.nextLine().toUpperCase();
//            System.out.println(input);
            switch(input)
            {
                case "CONTINUE":
                    playerPosition++;
                    System.out.println("");
                    break;
                case "HELP":
                case "?":
                    System.out.println("Type continue to move the story forward\n");
                    break; 
                
                default:
                    System.out.println("input is not valid (try contunue to move the story forward)\n");
                    break;
            }
        }

    }
    
    public static void enterDungen()
    {
        /*
        this method set up the middle of the dungeon and allows the player
        to move back and forth in the dungen it also set the combat up 
        @ auther Callum & william 
        */
        String input;
        while(playerPosition >3 && playerPosition <15)
        {
            printDescription();
//            System.out.println(input);
            setMonster();
            while(haveMonster())
            {
                printTargets();
                inCombat();
                //call caobat stuff
                if(!playerAlive)
                {
                    break;
                }
            }
            if(!playerAlive)
            {
                break;
            }
            boolean isIncorrect = true;
            switch (playerPosition)
            {
                case 5:
                    System.out.println("There are 4 doors here, which would you like to enter?");
                    input = SCAN.nextLine().toUpperCase();
                    
                    while (isIncorrect)
                    {
                        if(input.equals("BACK"))
                        {
                            playerPosition = roomLayout.get(playerPosition).get(0);
                            System.out.println("");
                            isIncorrect = false;
                        }
                        else
                        {
                            try
                            {
                                if (Integer.parseInt(input) > 0 && Integer.parseInt(input)<5)
                                {
                                    playerPosition = roomLayout.get(playerPosition).get(Integer.parseInt(input));
                                    System.out.println("");
                                    isIncorrect = false;
                                }
                                else
                                {
                                    System.out.println("Invalid number, try again");
                                    input = SCAN.nextLine().toUpperCase();
                                }
                            }
                            catch (NumberFormatException e)
                            {
                                System.out.println("Invalid input, try again");
                                input = SCAN.nextLine().toUpperCase();
                            }
                        }
                        
                    }
                    break;
                case 6:;
                case 7:;
                case 8:
                    System.out.println("There's no other paths here");
                    input = SCAN.nextLine().toUpperCase();
                    isIncorrect = true;
                    while (isIncorrect)
                    {
                        if (input.equals("BACK"))
                        {
                            playerPosition = roomLayout.get(playerPosition).get(0);
                            System.out.println("");
                            isIncorrect = false;
                        }
                        else
                        {
                            System.out.println("Invalid input, try again. Try typing back");
                            input = SCAN.nextLine().toUpperCase();
                        }
                    }
                    break;
                    
                default:
                    
                    if (playerPosition == 4)
                    {
                        System.out.println("Theres only one way, continue?");
                    }
                    else
                    {
                        System.out.println("Continue or go back?");
                    }
                    
                    input = SCAN.nextLine().toUpperCase();
                    isIncorrect = true;
                    while (isIncorrect)
                    {
                        switch(input)
                        {
                            case "CONTINUE":
                                playerPosition = roomLayout.get(playerPosition).get(1);
                                System.out.println("");
                                isIncorrect = false;
                                break;
                            case "HELP":
                            case "?":
                                System.out.println("Type continue to move the story forward");
                                break; 
                            case "BACK":
                                if (playerPosition > 4)
                                {
                                    playerPosition = roomLayout.get(playerPosition).get(0);
                                    System.out.println("");
                                    isIncorrect = false;
                                    break;
                                }
                                
                            default:
                                System.out.println("input is not valid (try contunue to move the story forward)");
                                input = SCAN.nextLine().toUpperCase();
                                break;
                        }
                    
                    }

                    break;
            }
            player.setHp(player.getHp()+1);
            saveGame();
        }
    }
    
    public static void storyEnd()
    {
         /**
          * this method sets up the last 3 rooms of the game and stops them from
          * moving back in the dungeon (only one way farward from here)
          * @auther Callum 
          * 
          
          */
   
        while(playerPosition > 14 && playerPosition < totalRoomNumber)
        {
            printDescription();
            String input = SCAN.nextLine().toUpperCase();
//            System.out.println(input);
            switch(input)
            {
                case "CONTINUE":
                    playerPosition++;
                    System.out.println("");
                    break;
                case "HELP":
                case "?":
                    System.out.println("Type continue to move the story forward\n");
                    break; 
                
                default:
                    System.out.println("input is not valid (try contunue to move the story forward)\n");
                    break;
            }
        }
    }
    
}

