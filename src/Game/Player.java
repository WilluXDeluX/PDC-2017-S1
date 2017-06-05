/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *this class is set up for the player to gets exp and it also gets the perseption
 * for perseption rolls
 * @author William
 */
public class Player extends Unit {
    
    private int exp;
    private int perception;

    public Player(String name, int[] stats)
    {
        super(name, stats);
        perception = stats[8];
        exp = 0;
        setType(0);
    }
    
    public int getExp()
    {
        return exp;
    }
    
    public void addExp(int ammount)
    {
        exp += ammount;
    }
    
    public void setExp(int exp)
    {
        this.exp = exp;
    }
    
    public int getPerception() {
        return perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }
    
    public void updateStats()
    {
        getStats()[0] = getHp();
        getStats()[1] = getAttack();
        getStats()[2] = getArmor();
        getStats()[3] = getStrength();
        getStats()[4] = getDexterity();
        getStats()[5] = getWisdom();
        getStats()[6] = getConstitution();
        getStats()[7] = getIntelect();
        getStats()[8] = perception;
    }
}
