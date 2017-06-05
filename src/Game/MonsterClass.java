/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *this class sets uo the statistics for the monsters for them to be called by
 * the main file 
 * @author Callum & William (edits)
 */
public class MonsterClass 
{
 
    private static int[] stats;
    
    public static int[] orc ()
    {
        stats = new int[9];
        stats [0] = 24; // hp 
        stats [1] = 4;  //plus to hit
        stats [2] = 11; //AC
        stats [3] = 17; //str
        stats [4] = 11;  // dex
        stats [5] = 7; //wiz
        stats [6] = 12; //con
        stats [7] = 8; //int
        
        return stats;   
    }
    public static int[] gnoll ()
    {
        stats = new int[9];
        stats [0] = 17; // hp 
        stats [1] = 3;  //plus to hit
        stats [2] = 13; //AC
        stats [3] = 15; //str
        stats [4] = 10;  // dex
        stats [5] = 11; //wiz
        stats [6] = 13; //con
        stats [7] = 8; //int
        
        return stats; 
    }
        public static int[] goblin ()
    {
        stats = new int[9];
        stats [0] = 8; // hp 
        stats [1] = 2;  //plus to hit
        stats [2] = 13; //AC
        stats [3] = 11; //str
        stats [4] = 13;  // dex
        stats [5] = 9; //wiz
        stats [6] = 12; //con
        stats [7] = 10; //int
        
        return stats; 
    }
    public static int[] kobold ()
    {
        stats = new int[9];
        stats [0] = 8; // hp 
        stats [1] = 2;  //plus to hit
        stats [2] = 13; //AC
        stats [3] = 9; //str
        stats [4] = 13;  // dex
        stats [5] = 8; //wiz
        stats [6] = 10; //con
        stats [7] = 10; //int
        
        return stats;
    }
}
