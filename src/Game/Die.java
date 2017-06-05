/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.Random;

/**
 *this class is a die it sets up the die with the ammount of faces on it
 * @author William
 */
public class Die {
    private int faces;
    private Random generator = new Random();
    
    public Die (int faces)
    {
        this.faces = faces;
    }
    
    public int newRoll ()
    {
        int roll = 1 + generator.nextInt(faces);
        return roll;
    }

}
