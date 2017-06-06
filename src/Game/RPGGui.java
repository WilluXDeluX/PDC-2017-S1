/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Callum
 */
public class RPGGui extends javax.swing.JFrame {

    Dimension frameSize;
    
    private final ArrayList<ArrayList<Boolean>> DEFAULTMONSTERS = new ArrayList<>();
    private final Die D20 = new Die(20); 
    private final Die D8 = new Die(8);
    private final Die D6 = new Die(6);
    private final Die D4 = new Die(4);
    private final Scanner SCAN = new Scanner(System.in);
    private ArrayList<ArrayList<Integer>> roomLayout = new ArrayList<>(); 
    private ArrayList<ArrayList<String>> description = new ArrayList<>();
    private ArrayList<ArrayList<Boolean>> monsters = new ArrayList<>();
    private int totalRoomNumber;
    private Player player;
    private int playerPosition = 0;
    private ArrayList<Monster> currentMonsters = new ArrayList<>();
    private boolean playerAlive = true;
    private boolean gameActive = true;
    private String gameText = "";
    /**
     * Creates new form RPGGui
     */
    public RPGGui() {
        initComponents();
        initialSetup();
        
        
//        while(playerAlive)
//        {
//            
//        }
        
    }
    
    private void initialSetup()
    {
        frameSize = this.getSize();
        this.add(startPanel);
        startPanel.setLocation(0,0);
        startPanel.setSize(280,300);
        setNewGameComponentVisible(false);
        setStartVisibility(false);
    }
    
    private void setNewGameComponentVisible(boolean visible)
    {
        enterNameLabel.setVisible(visible);
        nameField.setVisible(visible);
        selectClassLabel.setVisible(visible);
        selectClass.setVisible(visible);
        confirmButton.setVisible(visible);
        newGameButton.setVisible(!visible);
        loadGameButton.setVisible(!visible);
        exitGameButton.setVisible(!visible);
    }

    private void setStartVisibility(boolean visible)
    {
        gamePanel.setVisible(visible);
        startPanel.setVisible(!visible);
        if (visible)
        {
            this.setSize(frameSize);
        }
        else
        {
            this.setSize(startPanel.getSize());
        }
        setAllMonsters(!visible);
    }
    
    /**
     * this method sets up a new die and takes the damage off the defender
     * @param defender
     * @return damage
     */
    public int d8Damage(Unit defender)
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
    public int d6Damage(Unit defender)
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
    
    public int d4Damage(Unit defender)
    {
        int damage = D4.newRoll();
        int currentHP = defender.getHp();
        defender.setHp(currentHP - damage);
        return damage;
    }
    
     /**
     * this method takes in the attacker and defender and rolls a die to find out if
     * they hit or not 
     * @param attacker
     * @param defender
     * @return whether the unit hits or misses
     */
    public boolean d20Check(Unit attacker, Unit defender)
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
    public void attack(Unit attacker, Unit defender)
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
    
    public void loading() throws IOException
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
    
    public  void addMonsters()
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
    
    public boolean haveMonster()
    {
        /**
         * this method is to see if there is a monster in the room 
         */
        boolean haveMonster;
        haveMonster = monsters.get(playerPosition).contains(true);
        return haveMonster;
    }
    
    public void setMonster()
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

    public void removeMonster(int index)
            
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
    
    
    
    public boolean isMonsterAlive (Monster monster)
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
    
    public void printTargets()
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
    
    public void inCombat()
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
    
    public void isPlayerAlive()
    {
        if(player.getHp() <= 0)
        {
            playerAlive = false;
        }
    }
    
    public void printDescription()
    {
        ArrayList<String> currentDescription = description.get(playerPosition);
//        int index = 0;
        for (String d: currentDescription)
        {
            gameText += d;
//            index++;
        }
    }
    
    public void startDungeon()
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
    
    public void enterDungen()
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
//            saveGame();
        }
    }
    
    public void storyEnd()
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
                    System.out.println("input is not valid (try continue to move the story forward)\n");
                    break;
            }
        }
    }
    
    public void attackBack()
    {
        for (int i = 0; i < currentMonsters.size(); i++)
        {
            attack(currentMonsters.get(i), player);
            isPlayerAlive();
            if (!playerAlive)
            {
                break;
            }
        }
        gameText += "You have failed to live through this and was forever\nforgotten, the dungeon awaits another adventurer to\nseek out its secrets.\nPress continue to go to starting menu.";
        backb.setEnabled(false);
        saveGameButton.setEnabled(false);
        setAllMonsters(false);
        textAreaUpdate();
    }
    
    public void setAllMonsters(boolean visible)
    {
        goblinImageLabel.setVisible(visible);
        goblinHealthBar.setVisible(visible);
        gnollImageLabel.setVisible(visible);
        gnollHealthBar.setVisible(visible);
        orcImageLabel.setVisible(visible);
        orcHealthBar.setVisible(visible);
        koboldImageLabel.setVisible(visible);
        koboldHealthBar.setVisible(visible);
    }
    
    public void textAreaUpdate()
    {
        textArea.setText(gameText);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startPanel = new javax.swing.JPanel();
        enterNameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        selectClassLabel = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();
        selectClass = new javax.swing.JComboBox<>();
        newGameButton = new javax.swing.JButton();
        loadGameButton = new javax.swing.JButton();
        exitGameButton = new javax.swing.JButton();
        gamePanel = new javax.swing.JPanel();
        saveGameButton = new javax.swing.JButton();
        path1b = new javax.swing.JButton();
        path2b = new javax.swing.JButton();
        path3b = new javax.swing.JButton();
        path4b = new javax.swing.JButton();
        continueb = new javax.swing.JButton();
        backb = new javax.swing.JButton();
        sPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        playerImageLabel = new javax.swing.JLabel();
        gnollImageLabel = new javax.swing.JLabel();
        goblinImageLabel = new javax.swing.JLabel();
        koboldImageLabel = new javax.swing.JLabel();
        orcImageLabel = new javax.swing.JLabel();
        playerHealthBar = new javax.swing.JProgressBar();
        orcHealthBar = new javax.swing.JProgressBar();
        gnollHealthBar = new javax.swing.JProgressBar();
        goblinHealthBar = new javax.swing.JProgressBar();
        koboldHealthBar = new javax.swing.JProgressBar();
        saveReminder = new javax.swing.JLabel();
        attackHelp = new javax.swing.JLabel();
        playerNameLabel = new javax.swing.JLabel();
        backgroundLabel = new javax.swing.JLabel();

        startPanel.setMaximumSize(new java.awt.Dimension(280, 300));
        startPanel.setMinimumSize(new java.awt.Dimension(280, 300));
        startPanel.setPreferredSize(new java.awt.Dimension(280, 300));
        startPanel.setLayout(null);

        enterNameLabel.setText("Please enter your name:");
        startPanel.add(enterNameLabel);
        enterNameLabel.setBounds(60, 40, 150, 14);
        startPanel.add(nameField);
        nameField.setBounds(60, 70, 140, 30);

        selectClassLabel.setText("Select Class");
        startPanel.add(selectClassLabel);
        selectClassLabel.setBounds(60, 110, 130, 14);

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        startPanel.add(confirmButton);
        confirmButton.setBounds(60, 190, 143, 40);

        selectClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Barbarian", "Cleric", "Ranger", "Mage" }));
        startPanel.add(selectClass);
        selectClass.setBounds(60, 140, 140, 30);

        newGameButton.setText("NEW GAME");
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameButtonActionPerformed(evt);
            }
        });
        startPanel.add(newGameButton);
        newGameButton.setBounds(60, 40, 143, 54);

        loadGameButton.setText("LOAD GAME");
        loadGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadGameButtonActionPerformed(evt);
            }
        });
        startPanel.add(loadGameButton);
        loadGameButton.setBounds(60, 110, 143, 51);

        exitGameButton.setText("EXIT GAME");
        exitGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitGameButtonActionPerformed(evt);
            }
        });
        startPanel.add(exitGameButton);
        exitGameButton.setBounds(60, 180, 143, 55);

        startPanel.getAccessibleContext().setAccessibleName("");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        gamePanel.setLayout(null);

        saveGameButton.setText("Save Game");
        saveGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGameButtonActionPerformed(evt);
            }
        });
        gamePanel.add(saveGameButton);
        saveGameButton.setBounds(10, 30, 100, 23);

        path1b.setBackground(new java.awt.Color(20, 20, 20));
        path1b.setFont(new java.awt.Font("MV Boli", 1, 12)); // NOI18N
        path1b.setForeground(new java.awt.Color(240, 240, 240));
        path1b.setText("Path 1");
        path1b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                path1bActionPerformed(evt);
            }
        });
        gamePanel.add(path1b);
        path1b.setBounds(10, 530, 90, 30);

        path2b.setBackground(new java.awt.Color(20, 20, 20));
        path2b.setFont(new java.awt.Font("MV Boli", 1, 12)); // NOI18N
        path2b.setForeground(new java.awt.Color(240, 240, 240));
        path2b.setText("Path 2");
        path2b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                path2bActionPerformed(evt);
            }
        });
        gamePanel.add(path2b);
        path2b.setBounds(120, 530, 90, 30);

        path3b.setBackground(new java.awt.Color(20, 20, 20));
        path3b.setFont(new java.awt.Font("MV Boli", 1, 12)); // NOI18N
        path3b.setForeground(new java.awt.Color(240, 240, 240));
        path3b.setText("Path 3");
        path3b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                path3bActionPerformed(evt);
            }
        });
        gamePanel.add(path3b);
        path3b.setBounds(10, 573, 90, 30);

        path4b.setBackground(new java.awt.Color(20, 20, 20));
        path4b.setFont(new java.awt.Font("MV Boli", 1, 12)); // NOI18N
        path4b.setForeground(new java.awt.Color(240, 240, 240));
        path4b.setText("Path 4");
        path4b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                path4bActionPerformed(evt);
            }
        });
        gamePanel.add(path4b);
        path4b.setBounds(120, 573, 90, 30);

        continueb.setBackground(new java.awt.Color(20, 20, 20));
        continueb.setFont(new java.awt.Font("MV Boli", 1, 24)); // NOI18N
        continueb.setForeground(new java.awt.Color(240, 240, 240));
        continueb.setText("CONTINUE");
        continueb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                continuebMouseClicked(evt);
            }
        });
        continueb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuebActionPerformed(evt);
            }
        });
        gamePanel.add(continueb);
        continueb.setBounds(10, 530, 200, 74);

        backb.setBackground(new java.awt.Color(20, 20, 20));
        backb.setFont(new java.awt.Font("MV Boli", 1, 24)); // NOI18N
        backb.setForeground(new java.awt.Color(240, 240, 240));
        backb.setText("BACK");
        backb.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbActionPerformed(evt);
            }
        });
        gamePanel.add(backb);
        backb.setBounds(240, 530, 200, 74);

        sPane.setBackground(new java.awt.Color(0, 0, 0));
        sPane.setFocusable(false);
        sPane.setFont(new java.awt.Font("MV Boli", 1, 24)); // NOI18N

        textArea.setEditable(false);
        textArea.setBackground(new java.awt.Color(245, 245, 245));
        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Papyrus", 0, 13)); // NOI18N
        textArea.setLineWrap(true);
        textArea.setRows(20);
        textArea.setText(gameText);
        textArea.setWrapStyleWord(true);
        sPane.setViewportView(textArea);

        gamePanel.add(sPane);
        sPane.setBounds(10, 66, 430, 450);

        playerImageLabel.setRequestFocusEnabled(false);
        gamePanel.add(playerImageLabel);
        playerImageLabel.setBounds(610, 50, 100, 150);
        playerImageLabel.getAccessibleContext().setAccessibleName("playericon");

        gnollImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/800c9f6f5d6b1f71b4db354f0c72de38.jpg"))); // NOI18N
        gnollImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gnollImageLabelMouseClicked(evt);
            }
        });
        gamePanel.add(gnollImageLabel);
        gnollImageLabel.setBounds(680, 430, 100, 150);

        goblinImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/goblin.jpg"))); // NOI18N
        goblinImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goblinImageLabelMouseClicked(evt);
            }
        });
        gamePanel.add(goblinImageLabel);
        goblinImageLabel.setBounds(530, 250, 100, 150);

        koboldImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/kobold.jpg"))); // NOI18N
        koboldImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                koboldImageLabelMouseClicked(evt);
            }
        });
        gamePanel.add(koboldImageLabel);
        koboldImageLabel.setBounds(680, 250, 100, 150);

        orcImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ork.jpg"))); // NOI18N
        orcImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orcImageLabelMouseClicked(evt);
            }
        });
        gamePanel.add(orcImageLabel);
        orcImageLabel.setBounds(530, 430, 100, 150);
        orcImageLabel.getAccessibleContext().setAccessibleName("monster4");

        gamePanel.add(playerHealthBar);
        playerHealthBar.setBounds(610, 30, 100, 14);

        orcHealthBar.setMaximum(24);
        gamePanel.add(orcHealthBar);
        orcHealthBar.setBounds(530, 410, 100, 14);

        gnollHealthBar.setMaximum(17);
        gamePanel.add(gnollHealthBar);
        gnollHealthBar.setBounds(680, 410, 100, 14);

        goblinHealthBar.setMaximum(8);
        gamePanel.add(goblinHealthBar);
        goblinHealthBar.setBounds(530, 230, 100, 14);

        koboldHealthBar.setMaximum(8);
        gamePanel.add(koboldHealthBar);
        koboldHealthBar.setBounds(680, 230, 100, 14);

        saveReminder.setForeground(new java.awt.Color(200, 200, 200));
        saveReminder.setText("Please remember to save manually after battle! You cannot save during battle!");
        gamePanel.add(saveReminder);
        saveReminder.setBounds(10, 10, 450, 14);

        attackHelp.setForeground(new java.awt.Color(200, 200, 200));
        attackHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        attackHelp.setText("**Click on the enemy to attack!!**");
        gamePanel.add(attackHelp);
        attackHelp.setBounds(550, 210, 210, 14);

        playerNameLabel.setForeground(new java.awt.Color(200, 200, 200));
        playerNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gamePanel.add(playerNameLabel);
        playerNameLabel.setBounds(570, 0, 180, 20);

        backgroundLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/game-backround.jpg"))); // NOI18N
        gamePanel.add(backgroundLabel);
        backgroundLabel.setBounds(0, 0, 860, 610);
        backgroundLabel.getAccessibleContext().setAccessibleName("backround");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 863, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void continuebMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_continuebMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_continuebMouseClicked

    private void exitGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitGameButtonActionPerformed
        System.exit(0);
       // TODO add your handling code here:
    }//GEN-LAST:event_exitGameButtonActionPerformed

    private void goblinImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goblinImageLabelMouseClicked
        // TODO add your handling code here:
        attack(player, currentMonsters.get(0));
        if(!isMonsterAlive(currentMonsters.get(0)))
        {
            gameText += "\nYou have killed the goblin!";
            goblinImageLabel.setVisible(false);
            goblinHealthBar.setValue(8);
            goblinHealthBar.setVisible(false);
            removeMonster(0);
            backb.setEnabled(!haveMonster());
            continueb.setEnabled(!haveMonster());
            saveGameButton.setEnabled(!haveMonster());
        }
        else
        {
            goblinHealthBar.setValue(currentMonsters.get(0).getHp());
        }
    }//GEN-LAST:event_goblinImageLabelMouseClicked

    private void koboldImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_koboldImageLabelMouseClicked
        // TODO add your handling code here:
        attack(player, currentMonsters.get(1));
        if(!isMonsterAlive(currentMonsters.get(1)))
        {
            gameText += "\nYou have killed the kobold!";
            koboldImageLabel.setVisible(false);
            koboldHealthBar.setValue(8);
            koboldHealthBar.setVisible(false);
            removeMonster(1);
            backb.setEnabled(!haveMonster());
            continueb.setEnabled(!haveMonster());
            saveGameButton.setEnabled(!haveMonster());
        }
        else
        {
            koboldHealthBar.setValue(currentMonsters.get(1).getHp());
        }
    }//GEN-LAST:event_koboldImageLabelMouseClicked

    private void orcImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orcImageLabelMouseClicked
        // TODO add your handling code here:
        attack(player, currentMonsters.get(3));
        if(!isMonsterAlive(currentMonsters.get(3)))
        {
            gameText += "\nYou have killed the Orc!";
            orcImageLabel.setVisible(false);
            orcHealthBar.setValue(24);
            orcHealthBar.setVisible(false);
            removeMonster(3);
            backb.setEnabled(!haveMonster());
            continueb.setEnabled(!haveMonster());
            saveGameButton.setEnabled(!haveMonster());
        }
        else
        {
            orcHealthBar.setValue(currentMonsters.get(3).getHp());
        }
    }//GEN-LAST:event_orcImageLabelMouseClicked

    private void gnollImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gnollImageLabelMouseClicked
        // TODO add your handling code here:
        attack(player, currentMonsters.get(2));
        if(!isMonsterAlive(currentMonsters.get(2)))
        {
            gameText += "\nYou have killed the gnoll!";
            gnollImageLabel.setVisible(false);
            gnollHealthBar.setValue(17);
            gnollHealthBar.setVisible(false);
            removeMonster(2);
            backb.setEnabled(!haveMonster());
            continueb.setEnabled(!haveMonster());
            saveGameButton.setEnabled(!haveMonster());
        }
        else
        {
            gnollHealthBar.setValue(currentMonsters.get(2).getHp());
        }
    }//GEN-LAST:event_gnollImageLabelMouseClicked

    private void backbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backbActionPerformed

    private void path4bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_path4bActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_path4bActionPerformed

    private void path2bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_path2bActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_path2bActionPerformed

    private void path3bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_path3bActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_path3bActionPerformed

    private void path1bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_path1bActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_path1bActionPerformed

    private void continuebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuebActionPerformed
        // TODO add your handling code here:
        if(playerPosition <=3)
        {
            printDescription();
        }
        if(haveMonster())
        {
            backb.setEnabled(!haveMonster());
            continueb.setEnabled(!haveMonster());
            saveGameButton.setEnabled(!haveMonster());
        }
    }//GEN-LAST:event_continuebActionPerformed

    private void saveGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGameButtonActionPerformed
        // TODO add your handling code here:
        player.updateStats();
        String monsters = "";
        for (int i = 0; i < this.monsters.size(); i++)
        {
            monsters +="\n"+this.monsters.get(i);
        }
        
        Connection conn = null;
        String url = "jdbc:derby:GameDB;";
        String username = "pdc";
        String password = "pdc";
        Statement statement;
        
        String changeName = "UPDATE SAVEGAME SET PLAYERNAME = '"+ player.getName()+ "' WHERE PLAYERNAME IS NOT null";
        String changeDamageClass = "UPDATE SAVEGAME SET DAMAGECLASS = "+ player.getDamageClass()+ " WHERE DAMAGECLASS IS NOT null";
        String changeStats = "UPDATE SAVEGAME SET STATS = '"+ Arrays.toString(player.getStats())+ "' WHERE STATS IS NOT null";
        String changeCurrentRoom = "UPDATE SAVEGAME SET CURRENTROOM = "+ playerPosition+ " WHERE CURRENTROOM IS NOT null";
        String changeMonsters = "UPDATE SAVEGAME SET MONSTERS = '"+monsters+ "' WHERE MONSTERS IS NOT null";
        
        try{
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();
            statement.executeUpdate(changeName);
            statement.executeUpdate(changeDamageClass);
            statement.executeUpdate(changeStats);
            statement.executeUpdate(changeCurrentRoom);
            statement.executeUpdate(changeMonsters);
            
        } catch (SQLException ex){
          System.err.println("SQL Exception for Saving: " + ex.getMessage());   
        }
        
    }//GEN-LAST:event_saveGameButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
        
        if(!nameField.getText().equals(""))
        {   
            playerNameLabel.setText(nameField.getText());
            switch(selectClass.getSelectedIndex())
            {
                case 0:
                    player = new Player(nameField.getText(), PlayerClass.barb());
                    playerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/barb.jpg")));
                    playerHealthBar.setMaximum(82);
                    player.setDamageClass(8);
                    break;
                case 1:
                    player = new Player(nameField.getText(), PlayerClass.cleric());
                    playerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cleric.jpg")));
                    playerHealthBar.setMaximum(61);
                    player.setDamageClass(6);
                    break;
                case 2:
                    player = new Player(nameField.getText(), PlayerClass.ranger());
                    playerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ranger.jpg")));
                    playerHealthBar.setMaximum(52);
                    player.setDamageClass(8);
                    break;
                case 3:
                    player = new Player(nameField.getText(), PlayerClass.mage());
                    playerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wizard.jpg")));
                    playerHealthBar.setMaximum(34);
                    player.setDamageClass(5);
                    break;
            }
            playerHealthBar.setValue(player.getHp());
//            playerImageLabel.setBounds(610, 50, 100, 150);
            
            setNewGameComponentVisible(false);
            setStartVisibility(true);
            try {
                loading();
            } catch (IOException ex) {
                Logger.getLogger(RPGGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            addMonsters();
            printDescription();
            textAreaUpdate();
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadGameButtonActionPerformed
        // TODO add your handling code here:
        Connection conn = null;
        String url = "jdbc:derby:GameDB;";
        String username = "pdc";
        String password = "pdc";
        String name = "";
        String stats = "";
        String monsters = "";
        int damageClass = 0;
        int currentRoom = 0;
        Statement statement;
        String sql = "SELECT * FROM SAVEGAME";
        String getCount = "SELECT COUNT(*) FROM SAVEGAME";
        
        
        try 
        {
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();
            
            ResultSet rs;
            rs = statement.executeQuery(getCount);
            rs.next();
            System.out.println(rs.getInt(1));
            
            rs = statement.executeQuery(sql);
            
            while(rs.next())
            {
                name = rs.getString(1);
                stats = rs.getString(2);
                damageClass = rs.getInt(3);
                currentRoom = rs.getInt(4);
                monsters = rs.getString(5);
            }
            int[] stat = Arrays.stream(stats.substring(1, stats.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();//credit to Saul on stackoverflow http://stackoverflow.com/questions/7646392/convert-string-to-int-array-in-java
            player = new Player(name, stat);
            player.setDamageClass(damageClass);
            playerPosition = currentRoom;
            this.monsters = null;
            for (int i = 0; i < totalRoomNumber; i++)
            {
                String[] monsterString = monsters.replace("[","").replace("]","").split(",\\s");
                Boolean[] monsterArray = Arrays.stream(monsterString).map(Boolean::parseBoolean).toArray(Boolean[]::new);//credit to ΦXocę 웃 Пepeúpa ツ on stackoverflow http://stackoverflow.com/questions/39873596/convert-array-of-strings-to-boolean-list-in-java
                ArrayList<Boolean> monsterList = new ArrayList<>(Arrays.asList(monsterArray));
                this.monsters.add(monsterList);
            }
        } catch (SQLException ex)
        {
          System.err.println("SQL Exception for Loading: " + ex.getMessage());   
        }
        setAllMonsters(false);
    }//GEN-LAST:event_loadGameButtonActionPerformed

    private void newGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameButtonActionPerformed
        // TODO add your handling code here:
        setNewGameComponentVisible(true);
    }//GEN-LAST:event_newGameButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RPGGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RPGGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RPGGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RPGGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RPGGui().setVisible(true);
            }
            
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel attackHelp;
    private javax.swing.JButton backb;
    private javax.swing.JLabel backgroundLabel;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton continueb;
    private javax.swing.JLabel enterNameLabel;
    private javax.swing.JButton exitGameButton;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JProgressBar gnollHealthBar;
    private javax.swing.JLabel gnollImageLabel;
    private javax.swing.JProgressBar goblinHealthBar;
    private javax.swing.JLabel goblinImageLabel;
    private javax.swing.JProgressBar koboldHealthBar;
    private javax.swing.JLabel koboldImageLabel;
    private javax.swing.JButton loadGameButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton newGameButton;
    private javax.swing.JProgressBar orcHealthBar;
    private javax.swing.JLabel orcImageLabel;
    private javax.swing.JButton path1b;
    private javax.swing.JButton path2b;
    private javax.swing.JButton path3b;
    private javax.swing.JButton path4b;
    private javax.swing.JProgressBar playerHealthBar;
    private javax.swing.JLabel playerImageLabel;
    private javax.swing.JLabel playerNameLabel;
    private javax.swing.JScrollPane sPane;
    private javax.swing.JButton saveGameButton;
    private javax.swing.JLabel saveReminder;
    private javax.swing.JComboBox<String> selectClass;
    private javax.swing.JLabel selectClassLabel;
    private javax.swing.JPanel startPanel;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
