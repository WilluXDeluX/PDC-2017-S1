/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *this class is to set the stats for the player to be used with the game
 * (it is alos set for the future when this goes to  GUI) 
 * @author Callum & William (edits)
 */
public class PlayerClass
{
//    private int hp;
    private static int[]stats;
//    private int attack;
//    private int armor;
//    private int strength;
//    private int dexterity;
//    private int wisdom;
//    private int constitution;
//    private int perception;
//    private int interlect;
    
      public static int[] ranger ()
    {
        stats = new int[9];
        stats [0] = 15; // hp 
        stats [1] = 4;  //plus to hit
        stats [2] = 17; //AC
        stats [3] = 8; //str
        stats [4] = 18;  // dex
        stats [5] = 10; //wiz
        stats [6] = 16; //con
        stats [7] = 8; //int
        stats [8] = 18; // per
        
        return stats;
        
    }  public static int[] cleric ()
    {
        stats = new int[9];
        stats [0] = 22; // hp 
        stats [1] = 2;  //plus to hit
        stats [2] = 18; //AC
        stats [3] = 14; //str
        stats [4] = 8;  // dex
        stats [5] = 18; //wiz
        stats [6] = 18; //con
        stats [7] = 12; //int
        stats [8] = 10; // per
        
        return stats;
        
    }
      public static int[] mage ()
    {
        stats = new int[9];
        stats [0] = 12; // hp 
        stats [1] = 4;  //plus to hit
        stats [2] = 17; //AC
        stats [3] = 8; //str
        stats [4] = 8;  // dex
        stats [5] = 18; //wiz
        stats [6] = 10; //con
        stats [7] = 18; //int
        stats [8] = 16; // per
        
        return stats;
        
    }
    
    public static int[] barb ()

    {
        stats = new int[9];
        stats[0] = 20;
        stats[1] = 4;
        stats[2] = 18;
        stats[3] = 18;
        stats[4] = 16;
        stats[5] = 8;
        stats[6] = 18;
        stats[7] = 8;
        stats[8] = 10;
        
    
        return stats;
             
    }
  
}
